class GenericNumber<NumType> {
  zeroValue!: NumType; // ← ! le dice a TS: "confía en mí, lo asignaré después"
  add!: (x: NumType, y: NumType) => NumType;
}

let myGenericNumber = new GenericNumber<number>();
myGenericNumber.zeroValue = 0;

// ===== TODAS ESTAS FORMAS SON EQUIVALENTES =====

// 1. Función tradicional (tu forma actual)
myGenericNumber.add = function (x, y) {
  return x + y;
};

console.log("Suma usando la clase GenericNumber: 2 + 2 = " + myGenericNumber.add(2, 2));

// 2. Función flecha con llaves
myGenericNumber.add = (x, y) => {
  return x + y;
};

console.log("Suma usando la clase GenericNumber: 2 + 2 = " + myGenericNumber.add(2, 2));

// 3. Función flecha concisa (MÁS COMÚN)
myGenericNumber.add = (x, y) => x + y;

console.log("Suma usando la clase GenericNumber: 2 + 2 = " + myGenericNumber.add(2, 2));



// 4. Función flecha con tipos explícitos (innecesario pero válido)
myGenericNumber.add = (x: number, y: number): number => x + y;
console.log("Suma usando la clase GenericNumber: 2 + 2 = " + myGenericNumber.add(2, 2));
