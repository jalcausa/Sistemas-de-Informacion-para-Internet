class C {
  _length = 0;
  get length() {
    return this._length;
  }
  set length(value) {
    this._length = value;
  }
  toString(): String {
	return `C {length: ${this._length}}`
  }
}

/*
let a: C = new C();
console.log("Clase a: " + a.toString());
a.length = a.length + 1;
console.log("Clase a tras sumarle uno: " + a.toString());
*/

class Animal {
  move() {
    console.log("Moving along!");
  }
}

class Dog extends Animal {
  woof(times: number) {
    for (let i = 0; i < times; i++) {
      console.log("woof!");
    }
  }
}


let animal: Animal = new Animal();
console.log("Prueba del move de animal simple: ");
animal.move();

let perro: Dog = new Dog();
console.log("Prueba del uso de los métodos de perro");
console.log("Perro moviéndose: ");
perro.move();
console.log("Perro ladrnado tres veces: ");
perro.woof(3);