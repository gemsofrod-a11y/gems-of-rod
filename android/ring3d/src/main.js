// Point d'entrée embarqué dans l'app (WebView, page assets/ring3d/viewer.html).
// - window.ring.setConfig(json) : affiche la bague, qu'on fait tourner au doigt.
// - window.ring.renderSheet(json, lines) : compose la fiche technique 4 vues
//   (dessus, perspective, face, côté) avec cotes et bloc de texte, et la
//   renvoie à l'app en JPEG via AndroidRing.onSheet(dataUrl).
// L'app reçoit AndroidRing.onReady() quand le moteur est prêt, et
// AndroidRing.onError(message) si WebGL n'est pas disponible.
import { buildRing, makeRenderer, makeScene, orthoCamera, disposeObject, THREE } from './engine.js';
import { OrbitControls } from 'three/addons/controls/OrbitControls.js';

const bridge = window.AndroidRing || { onReady() {}, onSheet() {}, onError(m) { console.error(m); } };

let renderer, scene, camera, controls, ring = null;

function fitCamera() {
  const w = window.innerWidth, h = window.innerHeight;
  renderer.setSize(w, h, false);
  camera.aspect = w / h; camera.updateProjectionMatrix();
}

function init() {
  const canvas = document.getElementById('view');
  try {
    renderer = makeRenderer(canvas, window.innerWidth, window.innerHeight);
    renderer.setPixelRatio(Math.min(window.devicePixelRatio || 1, 2));
  } catch (e) {
    bridge.onError(String(e && e.message || e));
    return;
  }
  scene = makeScene(renderer);
  camera = new THREE.PerspectiveCamera(30, 1, 0.1, 400);
  camera.position.set(30, 26, 40);
  controls = new OrbitControls(camera, canvas);
  controls.target.set(0, 3, 0);
  controls.enablePan = false;
  controls.minDistance = 18; controls.maxDistance = 120;
  controls.autoRotate = true; controls.autoRotateSpeed = 1.2;
  controls.addEventListener('start', () => { controls.autoRotate = false; });
  fitCamera();
  window.addEventListener('resize', fitCamera);
  const loop = () => { controls.update(); renderer.render(scene, camera); requestAnimationFrame(loop); };
  loop();
  bridge.onReady();
}

function setConfig(json) {
  const cfg = typeof json === 'string' ? JSON.parse(json) : json;
  if (ring) { scene.remove(ring); disposeObject(ring); }
  ring = buildRing(cfg);
  scene.add(ring);
}

// ---------- Fiche technique 4 vues ----------
const SHEET_W = 2400, SHEET_H = 1700;
function fmt(mm) { return mm.toFixed(2).replace(/0$/, '').replace('.', ',') + ' mm'; }

function renderSheet(json, linesJson) {
  try {
    const cfg = typeof json === 'string' ? JSON.parse(json) : json;
    const lines = typeof linesJson === 'string' ? JSON.parse(linesJson) : (linesJson || []);
    const off = document.createElement('canvas');
    const r = makeRenderer(off, 10, 10);
    r.setClearColor(0xffffff, 0);
    const sc = makeScene(r);
    const model = buildRing(cfg); sc.add(model);
    const u = model.userData;

    const sheet = document.createElement('canvas'); sheet.width = SHEET_W; sheet.height = SHEET_H;
    const ctx = sheet.getContext('2d');
    ctx.fillStyle = '#ffffff'; ctx.fillRect(0, 0, SHEET_W, SHEET_H);

    const shot = (cam, x, y, w, h) => {
      r.setSize(w, h, false); r.render(sc, cam); ctx.drawImage(off, x, y, w, h);
      return (v) => { const p = new THREE.Vector3(...v).project(cam); return [x + (p.x + 1) / 2 * w, y + (1 - p.y) / 2 * h]; };
    };
    const headCy = (u.headTop + -(u.sh.Ri + u.sh.tBottom)) / 2;
    // dessus (haut gauche), perspective (haut droite), face (bas gauche), côté (bas droite)
    const topMap = shot(orthoCamera('top', 7.5, 840 / 560), 60, 120, 840, 560);
    const pc = new THREE.PerspectiveCamera(30, 1, 0.1, 400); pc.position.set(30, 26, 40); pc.lookAt(0, 2.5, 0);
    shot(pc, 1620, 60, 720, 720);
    const frontMap = shot(orthoCamera('front', 17, 820 / 820, headCy), 120, 820, 820, 820);
    const sideMap = shot(orthoCamera('side', 17, 560 / 820, headCy), 1720, 820, 560, 820);

    ctx.strokeStyle = '#6a6a6a'; ctx.fillStyle = '#2b2b2b'; ctx.lineWidth = 2; ctx.font = '30px sans-serif';
    const vLine = (x, y1, y2, label) => {
      ctx.beginPath(); ctx.moveTo(x, y1); ctx.lineTo(x, y2);
      ctx.moveTo(x - 10, y1); ctx.lineTo(x + 10, y1); ctx.moveTo(x - 10, y2); ctx.lineTo(x + 10, y2); ctx.stroke();
      ctx.fillText(label, x + 16, (y1 + y2) / 2 + 10);
    };
    const hLine = (y, x1, x2, label) => {
      ctx.beginPath(); ctx.moveTo(x1, y); ctx.lineTo(x2, y);
      ctx.moveTo(x1, y - 10); ctx.lineTo(x1, y + 10); ctx.moveTo(x2, y - 10); ctx.lineTo(x2, y + 10); ctx.stroke();
      const tw = ctx.measureText(label).width; ctx.fillText(label, (x1 + x2) / 2 - tw / 2, y + 44);
    };
    // largeur de la tête vue de dessus
    const box = new THREE.Box3();
    model.children.forEach((child, i) => { if (i > 0 && child.position.y > 0 && child.position.x === 0) box.expandByObject(child); });
    const headW = box.max.z - box.min.z;
    let a = topMap([0, 0, box.min.z]), b = topMap([0, 0, box.max.z]);
    vLine(940, a[1], b[1], fmt(headW));
    // hauteur de la tête et épaisseur du bas (face)
    const seat = u.sh.Ri + u.sh.tTop;
    a = frontMap([0, u.headTop, 0]); b = frontMap([0, seat, 0]);
    vLine(960, a[1], b[1], fmt(u.headTop - seat));
    a = frontMap([0, -u.sh.Ri, 0]); b = frontMap([0, -u.sh.Ri - u.sh.tBottom, 0]);
    vLine(960, a[1], b[1], fmt(u.sh.tBottom));
    // largeur du bas (côté)
    a = sideMap([0, -u.sh.Ri - u.sh.tBottom, -u.sh.wBottom / 2]); b = sideMap([0, -u.sh.Ri - u.sh.tBottom, u.sh.wBottom / 2]);
    hLine(a[1] + 30, a[0], b[0], fmt(u.sh.wBottom));

    // bloc de texte (lignes fournies par l'app, déjà traduites)
    let y = 780;
    lines.forEach((line, i) => {
      ctx.font = i === 0 ? 'bold 34px sans-serif' : '28px sans-serif';
      ctx.fillStyle = '#222';
      const words = String(line).split(' '); let cur = '';
      for (const wd of words) {
        const cand = cur ? cur + ' ' + wd : wd;
        if (ctx.measureText(cand).width > 640 && cur) { ctx.fillText(cur, 1000, y); y += 40; cur = wd; } else cur = cand;
      }
      if (cur) { ctx.fillText(cur, 1000, y); y += i === 0 ? 52 : 44; }
    });
    ctx.font = 'italic 44px serif'; ctx.fillStyle = '#7a5f22'; ctx.fillText('Gems of Rod', 60, 80);

    disposeObject(model); r.dispose();
    bridge.onSheet(sheet.toDataURL('image/jpeg', 0.92));
  } catch (e) {
    bridge.onError(String(e && e.message || e));
  }
}

window.ring = { setConfig, renderSheet };
window.addEventListener('load', init);
