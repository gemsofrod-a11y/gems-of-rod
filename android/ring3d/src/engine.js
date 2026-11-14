// Moteur 3D du compositeur de bague (Gems of Rod).
// Construit une vraie bague en 3D à partir de la composition saisie dans
// l'app : jonc effilé, tête (panier + griffes ou chaton), pierre centrale
// facettée de n'importe quelle taille, pierres annexes sur les épaules.
// Unités : millimètres. Axe du doigt = Z, sommet de la bague = +Y.
// Les contours des tailles reprennent ceux de RingStoneGeometry.kt.
import * as THREE from 'three';

// ---------- Contours normalisés (-1..1, x à droite, y « vers le bas » du dessin = +z ici) ----------
function cubic(p0, p1, p2, p3, n, out) {
  for (let i = 1; i <= n; i++) {
    const t = i / n, mt = 1 - t;
    out.push([mt * mt * mt * p0[0] + 3 * mt * mt * t * p1[0] + 3 * mt * t * t * p2[0] + t * t * t * p3[0],
      mt * mt * mt * p0[1] + 3 * mt * mt * t * p1[1] + 3 * mt * t * t * p2[1] + t * t * t * p3[1]]);
  }
}
function quad(p0, p1, p2, n, out) {
  for (let i = 1; i <= n; i++) {
    const t = i / n, mt = 1 - t;
    out.push([mt * mt * p0[0] + 2 * mt * t * p1[0] + t * t * p2[0], mt * mt * p0[1] + 2 * mt * t * p1[1] + t * t * p2[1]]);
  }
}
const ellipse = () => Array.from({ length: 96 }, (_, i) => { const a = -Math.PI / 2 + i / 96 * 2 * Math.PI; return [Math.cos(a), Math.sin(a)]; });
function roundedRect(c) {
  const o = [[-1 + c, -1], [1 - c, -1]]; quad([1 - c, -1], [1, -1], [1, -1 + c], 10, o); o.push([1, 1 - c]);
  quad([1, 1 - c], [1, 1], [1 - c, 1], 10, o); o.push([-1 + c, 1]); quad([-1 + c, 1], [-1, 1], [-1, 1 - c], 10, o); o.push([-1, -1 + c]);
  quad([-1, -1 + c], [-1, -1], [-1 + c, -1], 10, o); o.pop(); return o;
}
const stepRect = (c) => [[-1 + c, -1], [1 - c, -1], [1, -1 + c], [1, 1 - c], [1 - c, 1], [-1 + c, 1], [-1, 1 - c], [-1, -1 + c]];
const plainRect = () => [[-1, -1], [1, -1], [1, 1], [-1, 1]];
function marquise() { const o = [[0, -1]]; cubic([0, -1], [0.55, -1], [1, -0.45], [1, 0], 16, o); cubic([1, 0], [1, 0.45], [0.55, 1], [0, 1], 16, o); cubic([0, 1], [-0.55, 1], [-1, 0.45], [-1, 0], 16, o); cubic([-1, 0], [-1, -0.45], [-0.55, -1], [0, -1], 16, o); o.pop(); return o; }
function poire() { const o = [[0, -1]]; cubic([0, -1], [0.62, -0.82], [1, -0.1], [1, 0.32], 16, o); cubic([1, 0.32], [1, 0.74], [0.5, 1], [0, 1], 16, o); cubic([0, 1], [-0.5, 1], [-1, 0.74], [-1, 0.32], 16, o); cubic([-1, 0.32], [-1, -0.1], [-0.62, -0.82], [0, -1], 16, o); o.pop(); return o; }
function briolette() { const o = [[0, -1]]; cubic([0, -1], [0.42, -1.05], [1.05, -0.28], [0.72, 0.42], 16, o); cubic([0.72, 0.42], [0.5, 0.9], [-0.5, 0.9], [-0.72, 0.42], 16, o); cubic([-0.72, 0.42], [-1.05, -0.28], [-0.42, -1.05], [0, -1], 16, o); o.pop(); return o; }
function coeur() { const o = [[0, 0.78]]; cubic([0, 0.78], [-1, 0.12], [-1, -0.68], [0, -0.28], 24, o); cubic([0, -0.28], [1, -0.68], [1, 0.12], [0, 0.78], 24, o); o.pop(); return o; }
const kite = () => [[0, -1], [0.85, -0.1], [0, 1], [-0.85, -0.1]];
function trillion() { const o = [[0, -1]]; quad([0, -1], [1.05, 0.35], [1, 0.75], 14, o); quad([1, 0.75], [0, 0.55], [-1, 0.75], 14, o); quad([-1, 0.75], [-1.05, 0.35], [0, -1], 14, o); o.pop(); return o; }

const OUTLINES = {
  RONDE: ellipse, OVALE: ellipse, COUSSIN: () => roundedRect(0.42), PRINCESSE: plainRect, BAGUETTE: plainRect,
  ASSCHER: () => stepRect(0.16), EMERAUDE_CUT: () => stepRect(0.24), RADIANT: () => stepRect(0.12),
  MARQUISE: marquise, POIRE: poire, BRIOLETTE: briolette, COEUR: coeur, KITE: kite, TRILLION: trillion,
};
const STEP_CUTS = new Set(['BAGUETTE', 'ASSCHER', 'EMERAUDE_CUT', 'KITE']);
const DIAG = [[1, 1], [-1, 1], [1, -1], [-1, -1]];
const PRONGS = {
  RONDE: DIAG, OVALE: DIAG, COUSSIN: DIAG, ASSCHER: DIAG, EMERAUDE_CUT: DIAG, RADIANT: DIAG,
  PRINCESSE: DIAG.map(d => [...d, 'v']), BAGUETTE: DIAG.map(d => [...d, 'v']),
  MARQUISE: [[0, -1, 'v'], [0, 1, 'v'], [1, 0.35], [-1, 0.35], [1, -0.35], [-1, -0.35]],
  POIRE: [[0, -1, 'v'], [1, 0.35], [-1, 0.35], [0.55, 1], [-0.55, 1]],
  BRIOLETTE: [[0, -1, 'v'], [1, 0.4], [-1, 0.4]],
  COEUR: [[0, 1, 'v'], [1, -0.2], [-1, -0.2], [0, -0.3, 'in']],
  KITE: [[0, -1, 'v'], [1, -0.1, 'v'], [0, 1, 'v'], [-1, -0.1, 'v']],
  TRILLION: [[0, -1, 'v'], [1, 0.8, 'v'], [-1, 0.8, 'v']],
};

function centered(poly) {
  let x = 0, y = 0; poly.forEach(p => { x += p[0]; y += p[1]; });
  x /= poly.length; y /= poly.length; return poly.map(p => [p[0] - x, p[1] - y]);
}
function byAngle(poly, n, phase = 0) {
  const out = [];
  for (let i = 0; i < n; i++) {
    const a = -Math.PI / 2 + phase + i / n * 2 * Math.PI, dx = Math.cos(a), dy = Math.sin(a);
    let best = [0, 0], bt = 1e9;
    for (let j = 0; j < poly.length; j++) {
      const p = poly[j], q = poly[(j + 1) % poly.length], ex = q[0] - p[0], ey = q[1] - p[1];
      const den = dx * ey - dy * ex; if (Math.abs(den) < 1e-9) continue;
      const t = (p[0] * ey - p[1] * ex) / den, s = (p[0] * dy - p[1] * dx) / den;
      if (t > 0 && s >= -1e-5 && s <= 1 + 1e-5 && t < bt) { bt = t; best = [dx * t, dy * t]; }
    }
    out.push(best);
  }
  return out;
}
function supportPoint(poly, dx, dy) { let b = poly[0], bd = -1e9; for (const p of poly) { const d = p[0] * dx + p[1] * dy; if (d > bd) { bd = d; b = p; } } return b; }

// ---------- Matériaux ----------
const METALS = {
  PLATINE: { color: 0xdedfe2, roughness: 0.10 }, OR_BLANC: { color: 0xe6e3dc, roughness: 0.10 },
  OR_JAUNE: { color: 0xf3c35c, roughness: 0.13 }, OR_ROSE: { color: 0xeab097, roughness: 0.13 },
};
export function metalMaterial(metal) {
  const m = METALS[metal] || METALS.OR_JAUNE;
  return new THREE.MeshPhysicalMaterial({ color: m.color, metalness: 1, roughness: m.roughness, envMapIntensity: 1, clearcoat: 0.3, clearcoatRoughness: 0.08 });
}
function isNearWhite(hex) { const c = new THREE.Color(hex); const hsl = {}; c.getHSL(hsl); return hsl.s < 0.12 && hsl.l > 0.75; }
// Rendu « bijouterie » temps réel : facettes miroir qui reflètent le studio,
// semi-transparentes pour laisser voir le pavillon, irisation pour le feu.
export function gemMaterial(hex) {
  if (isNearWhite(hex)) {
    return new THREE.MeshPhysicalMaterial({ color: 0xffffff, metalness: 1, roughness: 0.02, envMapIntensity: 1.35,
      iridescence: 0.35, iridescenceIOR: 1.9, iridescenceThicknessRange: [180, 420], flatShading: true, side: THREE.DoubleSide,
      transparent: true, opacity: 0.72, depthWrite: false });
  }
  return new THREE.MeshPhysicalMaterial({ color: hex, metalness: 0.55, roughness: 0.03, envMapIntensity: 1.6,
    clearcoat: 1, clearcoatRoughness: 0, flatShading: true, side: THREE.DoubleSide });
}

// ---------- Géométrie des pierres ----------
// Pierre facettée de taille `cut`, largeur w (x) et longueur h (z), girdle en y = 0.
export function stoneGeometry(cut, w, h) {
  const poly = centered((OUTLINES[cut] || ellipse)());
  const d = (w + h) / 2;
  const step = STEP_CUTS.has(cut);
  const crown = (step ? 0.12 : 0.15) * d, pav = (step ? 0.40 : 0.43) * d, girdle = 0.02 * d;
  const P = (p, k, y) => [p[0] * k * w / 2, y, p[1] * k * h / 2];
  const verts = []; const tri = (a, b, c) => verts.push(...a, ...b, ...c);
  const quadF = (a, b, c, e) => { tri(a, b, c); tri(a, c, e); };
  const yT = girdle / 2 + crown, yG = girdle / 2, yGb = -girdle / 2, yC = -girdle / 2 - pav;
  if (step) {
    const base = poly.length <= 8 ? poly : byAngle(poly, 16);
    const crownRings = [[1, yG], [0.86, yG + crown * 0.5], [0.72, yT]].map(([k, y]) => base.map(p => P(p, k, y)));
    for (let l = 0; l < 2; l++) for (let i = 0; i < base.length; i++) { const j = (i + 1) % base.length; quadF(crownRings[l][i], crownRings[l][j], crownRings[l + 1][j], crownRings[l + 1][i]); }
    const top = crownRings[2]; for (let i = 1; i < top.length - 1; i++) tri(top[0], top[i], top[i + 1]);
    const gb = base.map(p => P(p, 1, yGb));
    for (let i = 0; i < base.length; i++) { const j = (i + 1) % base.length; quadF(crownRings[0][i], crownRings[0][j], gb[j], gb[i]); }
    const pavRings = [gb, base.map(p => P(p, 0.6, yGb - pav * 0.5)), base.map(p => P(p, 0.22, yC + pav * 0.1))];
    for (let l = 0; l < 2; l++) for (let i = 0; i < base.length; i++) { const j = (i + 1) % base.length; quadF(pavRings[l][j], pavRings[l][i], pavRings[l + 1][i], pavRings[l + 1][j]); }
    const bot = pavRings[2]; for (let i = 1; i < bot.length - 1; i++) tri(bot[0], bot[i + 1], bot[i]);
  } else {
    const N = 16;
    const G = byAngle(poly, 2 * N), T = byAngle(poly, N), S = byAngle(poly, N, Math.PI / N);
    for (let k = 0; k < N; k++) {
      const k1 = (k + 1) % N, km = (k - 1 + N) % N;
      const T0 = P(T[k], 0.57, yT), T1 = P(T[k1], 0.57, yT);
      const Sk = P(S[k], 0.8, yG + crown * 0.42), Sp = P(S[km], 0.8, yG + crown * 0.42);
      const G0 = P(G[2 * k], 1, yG), Gm = P(G[2 * k + 1], 1, yG), G1 = P(G[(2 * k + 2) % (2 * N)], 1, yG);
      tri([0, yT, 0], T1, T0);
      tri(T0, T1, Sk);
      tri(T0, Sk, G0); tri(T0, G0, Sp);
      tri(Sk, Gm, G0); tri(Sk, G1, Gm);
      const B0 = P(G[2 * k], 1, yGb), Bm = P(G[2 * k + 1], 1, yGb), B1 = P(G[(2 * k + 2) % (2 * N)], 1, yGb);
      quadF(G0, Gm, Bm, B0); quadF(Gm, G1, B1, Bm);
      const y1 = yC + pav * 0.45, M0 = P(T[k], 0.45, y1), M1 = P(T[k1], 0.45, y1), C = [0, yC, 0];
      tri(B0, Bm, M0); tri(Bm, B1, M1); tri(Bm, M1, M0); tri(M0, M1, C);
    }
  }
  const g = new THREE.BufferGeometry();
  g.setAttribute('position', new THREE.Float32BufferAttribute(verts, 3));
  g.computeVertexNormals();
  g.userData = { crown, pav, girdle, poly };
  return g;
}

// Étendue (x, z) d'une pierre w × h tournée de `deg` autour de y.
function extents(cut, w, h, deg) {
  const poly = (OUTLINES[cut] || ellipse)(), a = deg * Math.PI / 180, c = Math.cos(a), s = Math.sin(a);
  let minX = 1e9, maxX = -1e9, minZ = 1e9, maxZ = -1e9;
  for (const [u, v] of poly) { const x = u * w / 2, z = v * h / 2, X = x * c - z * s, Z = x * s + z * c; minX = Math.min(minX, X); maxX = Math.max(maxX, X); minZ = Math.min(minZ, Z); maxZ = Math.max(maxZ, Z); }
  return [maxX - minX, maxZ - minZ];
}

// ---------- Pièces de métal ----------
function tubeAlong(points, radius, material, closed = false) {
  const curve = new THREE.CatmullRomCurve3(points, closed);
  return new THREE.Mesh(new THREE.TubeGeometry(curve, Math.max(24, points.length * 12), radius, 12, closed), material);
}
// Muret (chaton clos) qui suit le contour `ring` (points 3D au même y), entre y0 et y1, épaisseur e vers l'extérieur.
function wallMesh(ring, e, y0, y1, material, from = 0, to = 1) {
  const n = ring.length, a = Math.floor(from * n), b = Math.ceil(to * n);
  const cx = ring.reduce((s, p) => s + p.x, 0) / n, cz = ring.reduce((s, p) => s + p.z, 0) / n;
  const pos = [], idx = [];
  const out = (p) => { const dx = p.x - cx, dz = p.z - cz, l = Math.hypot(dx, dz) || 1; return [p.x + dx / l * e, p.z + dz / l * e]; };
  let count = 0;
  for (let i = a; i <= b; i++) {
    const p = ring[i % n], o = out(p);
    pos.push(p.x, y0, p.z, p.x, y1, p.z, o[0], y1, o[1], o[0], y0, o[1]);
    count++;
  }
  for (let i = 0; i < count - 1; i++) for (let k = 0; k < 4; k++) {
    const p0 = i * 4 + k, p1 = i * 4 + (k + 1) % 4, q0 = (i + 1) * 4 + k, q1 = (i + 1) * 4 + (k + 1) % 4;
    idx.push(p0, q0, p1, p1, q0, q1);
  }
  const g = new THREE.BufferGeometry(); g.setAttribute('position', new THREE.Float32BufferAttribute(pos, 3)); g.setIndex(idx); g.computeVertexNormals();
  return new THREE.Mesh(g, material);
}
function bead(r, material) { return new THREE.Mesh(new THREE.SphereGeometry(r, 16, 12), material); }
function bar(len, h, d, material) { return new THREE.Mesh(new THREE.BoxGeometry(len, h, d), material); }

// Pierre + sertissage, dans un repère local (girdle en y = 0, x/z = plan de la pierre).
// seatY : hauteur (négative) du métal sur lequel repose la tête ; mini = pierre annexe.
function setStone(group, s, metal, seatY, mini) {
  const geo = stoneGeometry(s.cut, s.w, s.h);
  const { crown, pav, poly } = geo.userData;
  const holder = new THREE.Group(); holder.rotation.y = -(s.rot || 0) * Math.PI / 180; group.add(holder);
  holder.add(new THREE.Mesh(geo, gemMaterial(s.color)));
  const at = (p, k, y) => new THREE.Vector3(p[0] * k * s.w / 2, y, p[1] * k * s.h / 2);
  const ring48 = byAngle(poly, 48);
  const dRef = Math.min(s.w, s.h);
  const sert = s.sert;
  const bezel = sert === 'CLOS' || sert === 'DEMI_CLOS' || sert === 'RAS';
  // panier sous la pierre (sauf chaton, qui descend jusqu'au jonc)
  if (!mini && !bezel) {
    holder.add(tubeAlong(ring48.map(p => at(p, 0.62, -pav * 0.55)), Math.max(0.22, dRef * 0.035), metal, true));
  }
  if (sert === 'GRIFFES') {
    const pr = mini ? Math.max(0.16, dRef * 0.09) : Math.max(0.38, dRef * 0.055);
    for (const [dx, dy, style] of PRONGS[s.cut] || DIAG) {
      if (style === 'in') { const b = bead(pr * 1.1, metal); b.position.copy(at([0, -0.3 - 0], 1, crown * 0.4)); holder.add(b); continue; }
      const sp = supportPoint(poly, dx, dy);
      const r = style === 'v' ? pr * 1.25 : pr;
      if (mini) { const b = bead(r, metal); b.position.copy(at(sp, 1.0, crown * 0.35)); holder.add(b); continue; }
      const pts = [at(sp, 0.32, seatY), at(sp, 0.62, -pav * 0.55), at(sp, 1.04, -pav * 0.15), at(sp, 1.06, crown * 0.25), at(sp, 0.93, crown * 0.62)];
      holder.add(tubeAlong(pts, r, metal));
      const tip = bead(r * 1.05, metal); tip.position.copy(pts[pts.length - 1]); holder.add(tip);
    }
  } else if (bezel) {
    const ring = ring48.map(p => at(p, 1.0, 0));
    const e = mini ? 0.2 : Math.max(0.45, dRef * 0.06);
    const top = sert === 'RAS' ? crown * 0.05 : crown * 0.35;
    const bottom = mini ? -pav * 0.5 : seatY;
    if (sert === 'DEMI_CLOS') {
      holder.add(wallMesh(ring, e, bottom, top, metal, 0.12, 0.38));
      holder.add(wallMesh(ring, e, bottom, top, metal, 0.62, 0.88));
      if (!mini) holder.add(tubeAlong(ring48.map(p => at(p, 0.62, -pav * 0.55)), Math.max(0.22, dRef * 0.035), metal, true));
    } else {
      holder.add(wallMesh(ring, e, bottom, top, metal));
    }
  } else if (sert === 'RAIL' || sert === 'BARRETTE') {
    const isRail = sert === 'RAIL';
    for (const sx of [-1, 1]) {
      const b = bar(isRail ? 0.45 : 0.6, mini ? pav * 0.9 : pav * 1.2, s.h * (isRail ? 1.1 : 0.75), metal);
      b.position.set(sx * (s.w / 2 + (isRail ? 0.22 : 0.3)), crown * 0.1 - (mini ? pav * 0.45 : pav * 0.5), 0); holder.add(b);
    }
  } else {
    // pavé, grain, grappe, bright-cut : grains de métal autour du rondiste
    const n = { PAVE: mini ? 4 : 10, GRAIN: mini ? 4 : 8, CLUSTER: 6, BRIGHT_CUT: 8 }[sert] || 6;
    const k = { PAVE: 1.12, GRAIN: 1.06, CLUSTER: 1.16, BRIGHT_CUT: 1.1 }[sert] || 1.1;
    const pts = mini ? [[0.75, 0.75], [-0.75, 0.75], [0.75, -0.75], [-0.75, -0.75]].map(([u, v]) => [u * 1.15, v * 1.15]) : byAngle(poly, n);
    const r = mini ? Math.max(0.14, dRef * 0.12) : Math.max(0.22, dRef * 0.05);
    for (const p of pts) { const b = bead(r, metal); b.position.copy(at(p, mini ? 0.62 : k, crown * 0.2)); holder.add(b); }
  }
  return { crown, pav };
}

// ---------- Jonc ----------
function shankGeometry(o) {
  const N = 360, M = 40, pos = [], idx = [];
  const prof = (wHalf, t, rc) => {
    const pts = [];
    const corners = [[t - rc, wHalf - rc, 0], [rc, wHalf - rc, Math.PI / 2], [rc, -wHalf + rc, Math.PI], [t - rc, -wHalf + rc, 1.5 * Math.PI]];
    for (const [cx, cz, a0] of corners) for (let i = 0; i < M / 4; i++) { const a = a0 + i / (M / 4) * Math.PI / 2; pts.push([cx + rc * Math.cos(a), cz + rc * Math.sin(a)]); }
    return pts;
  };
  for (let i = 0; i <= N; i++) {
    const th = i / N * 2 * Math.PI, { w, t } = shankAt(o, th);
    for (const [r, z] of prof(w / 2, t, Math.min(w, t) * 0.38)) { const rr = o.Ri + r; pos.push(rr * Math.sin(th), rr * Math.cos(th), z); }
  }
  for (let i = 0; i < N; i++) for (let j = 0; j < M; j++) { const a = i * M + j, b = i * M + (j + 1) % M, c = (i + 1) * M + j, d = (i + 1) * M + (j + 1) % M; idx.push(a, c, b, b, c, d); }
  const g = new THREE.BufferGeometry(); g.setAttribute('position', new THREE.Float32BufferAttribute(pos, 3)); g.setIndex(idx); g.computeVertexNormals(); return g;
}
function shankAt(o, th) { const f = Math.pow(Math.max(0, Math.cos(th)), 2.2); return { w: o.wBottom + (o.wTop - o.wBottom) * f, t: o.tBottom + (o.tTop - o.tBottom) * f }; }

// ---------- Bague complète ----------
// cfg = { metal, grams, center:{cut,w,h,rot,color,sert}, side:{n,cut,w,h,color,sert,esp,off} | null }
export function buildRing(cfg) {
  const group = new THREE.Group();
  const metal = metalMaterial(cfg.metal);
  const g = Math.min(Math.max(cfg.grams || 3, 1), 15);
  const side = cfg.side && cfg.side.n > 0 ? cfg.side : null;
  const sideAcross = side ? extents(side.cut, side.w, side.h, side.off || 0)[1] : 0;
  const sh = {
    Ri: 8.65,
    wBottom: 1.3 + g * 0.1, tBottom: 1.0 + g * 0.08,
    wTop: Math.max(2.0 + g * 0.1, side ? sideAcross + 1.0 : 0), tTop: 1.5 + g * 0.1,
  };
  group.add(new THREE.Mesh(shankGeometry(sh), metal));
  const topY = sh.Ri + sh.tTop;
  const c = cfg.center;
  const cGeo = stoneGeometry(c.cut, c.w, c.h); const pavC = cGeo.userData.pav, crownC = cGeo.userData.crown;
  const yG = topY + 0.35 + pavC;
  const head = new THREE.Group(); head.position.y = yG; group.add(head);
  setStone(head, c, metal, topY - 0.4 - yG, false);

  if (side) {
    const R = sh.Ri + sh.tTop;
    const alongC = extents(c.cut, c.w, c.h, c.rot || 0)[0];
    const alongS = extents(side.cut, side.w, side.h, side.off || 0)[0];
    const gap = (alongC * 0.5 + alongS * 0.75) / R;
    const step = alongS * 1.15 * (0.8 + (side.esp ?? 0.15)) / R;
    for (const sgn of [-1, 1]) for (let i = 0; i < side.n; i++) {
      const th = sgn * Math.min(gap + i * step, 1.25);
      const { t } = shankAt(sh, th);
      const r = sh.Ri + t;
      const holder = new THREE.Group();
      holder.position.set(r * Math.sin(th), r * Math.cos(th), 0); holder.rotation.z = -th; group.add(holder);
      const sGeo = stoneGeometry(side.cut, side.w, side.h);
      const sunk = side.sert === 'PAVE' || side.sert === 'GRAIN' || side.sert === 'RAIL' ? -sGeo.userData.crown * 0.6 : 0.08;
      const inner = new THREE.Group(); inner.position.y = sunk; holder.add(inner);
      setStone(inner, { ...side, rot: side.off || 0 }, metal, -sGeo.userData.pav, true);
    }
  }
  group.userData = { sh, topY, yG, headTop: yG + crownC, center: c };
  return group;
}

// ---------- Studio ----------
function studioEnvironment() {
  const env = new THREE.Scene();
  env.add(new THREE.Mesh(new THREE.BoxGeometry(60, 60, 60), new THREE.MeshBasicMaterial({ color: 0x77777c, side: THREE.BackSide })));
  const panel = (w, h, x, y, z, k) => {
    const m = new THREE.Mesh(new THREE.PlaneGeometry(w, h), new THREE.MeshBasicMaterial({ color: new THREE.Color(1, 1, 1).multiplyScalar(k), side: THREE.DoubleSide }));
    m.position.set(x, y, z); m.lookAt(0, 0, 0); env.add(m);
  };
  panel(30, 30, 0, 29, 0, 3.2); panel(8, 40, -28, 6, 10, 4.5); panel(8, 40, 28, 6, -6, 3.5);
  panel(30, 6, 0, 2, 29, 2.4); panel(6, 6, 18, 18, 18, 8); panel(5, 5, -16, 20, -16, 7);
  let seed = 7; const rnd = () => (seed = (seed * 16807) % 2147483647) / 2147483647;
  for (let i = 0; i < 70; i++) {
    const th = rnd() * Math.PI * 2, ph = 0.15 + rnd() * 1.25, r = 27;
    const k = rnd() < 0.5 ? 0.05 : 2 + rnd() * 9, sz = 2 + rnd() * 4;
    panel(sz, sz, r * Math.sin(ph) * Math.cos(th), r * Math.cos(ph), r * Math.sin(ph) * Math.sin(th), k);
  }
  const floor = new THREE.Mesh(new THREE.PlaneGeometry(60, 60), new THREE.MeshBasicMaterial({ color: 0xb0b0b0 }));
  floor.rotation.x = -Math.PI / 2; floor.position.y = -29; env.add(floor);
  return env;
}
export function makeRenderer(canvas, w, h) {
  const r = new THREE.WebGLRenderer({ canvas, antialias: true, preserveDrawingBuffer: true, alpha: true });
  r.setPixelRatio(1); r.setSize(w, h, false);
  r.toneMapping = THREE.ACESFilmicToneMapping; r.toneMappingExposure = 1.05;
  r.outputColorSpace = THREE.SRGBColorSpace;
  return r;
}
export function makeScene(renderer) {
  const scene = new THREE.Scene();
  const pm = new THREE.PMREMGenerator(renderer);
  scene.environment = pm.fromScene(studioEnvironment(), 0.02).texture;
  pm.dispose();
  const key = new THREE.DirectionalLight(0xffffff, 1.6); key.position.set(-6, 14, 10); scene.add(key);
  const fill = new THREE.DirectionalLight(0xffffff, 0.6); fill.position.set(8, 4, -6); scene.add(fill);
  return scene;
}
export function orthoCamera(view, half, aspect, cy = 0) {
  const cam = new THREE.OrthographicCamera(-half * aspect, half * aspect, half, -half, 0.1, 200);
  if (view === 'top') { cam.position.set(0, 60, 0); cam.up.set(0, 0, -1); cam.lookAt(0, 0, 0); }
  if (view === 'front') { cam.position.set(0, cy, 60); cam.lookAt(0, cy, 0); }
  if (view === 'side') { cam.position.set(60, cy, 0); cam.lookAt(0, cy, 0); }
  return cam;
}
export function disposeObject(obj) {
  obj.traverse(o => { if (o.geometry) o.geometry.dispose(); if (o.material) (Array.isArray(o.material) ? o.material : [o.material]).forEach(m => m.dispose()); });
}
export { THREE };
