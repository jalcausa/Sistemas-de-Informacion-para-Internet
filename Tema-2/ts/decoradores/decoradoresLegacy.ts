// ===== DECORADORES =====
/*
RESUMEN DE TIPOS DE DECORADORES:

1) Decorador de clase:
Recibe: constructor
Usos: cambiar el constructor, modificar el prototipo, añadir propiedades estáticas.
Ejemplo en tu código: @immutable


2) Decorador de método:
Recibe: target, propertyName, descriptor
Usos: envolver el método, añadir validaciones, logging, modificar comportamiento.
Ejemplo en tu código: @logMethod, @validate

3) Decorador de propiedad:
Recibe: target, propertyName
Usos: redefinir get/set, hacer la propiedad de solo lectura, modificar el valor inicial.
Ejemplo en tu código: @readonly

*/

// 1. Decorador de clase para hacer inmutable
function immutable<T extends { new (...args: any[]): {} }>(constructor: T) {
  return class extends constructor {
    constructor(...args: any[]) {
      super(...args);
      Object.freeze(this); // Congela la instancia
    }
  };
}

// 2. Decorador de método para logging
function logMethod(
  target: any, // objeto sobre el que se aplica el decorador
  propertyName: string, // nombre del método o propiedad sobre el que se aplica el decorador
  descriptor: PropertyDescriptor // descriptor de propiedad, permite acceder al método original y reemplazarlo por la versión decorada
) {
  const originalMethod = descriptor.value;

  descriptor.value = function (...args: any[]) {
    console.log(
      `📞 [${new Date().toISOString()}] Llamando a ${propertyName}(${args.join(
        ", "
      )})`
    );
    const startTime = performance.now();

    try {
      const result = originalMethod.apply(this, args);
      const endTime = performance.now();
      console.log(
        `✅ [${new Date().toISOString()}] ${propertyName} completado en ${(
          endTime - startTime
        ).toFixed(2)}ms`
      );
      console.log(`📤 Resultado: ${result}`);
      return result;
    } catch (error) {
      // ✅ CAMBIO AQUÍ: Type guard para error
      if (error instanceof Error) {
        console.log(
          `❌ [${new Date().toISOString()}] Error en ${propertyName}:`,
          error.message
        );
      } else {
        console.log(
          `❌ [${new Date().toISOString()}] Error desconocido en ${propertyName}:`,
          error
        );
      }
      throw error;
    }
  };
}

// 3. Decorator factory para validación
function validate(validationType: "positive" | "string" | "object") {
  return function (
    target: any,
    propertyName: string,
    descriptor: PropertyDescriptor
  ) {
    const originalMethod = descriptor.value;

    descriptor.value = function (...args: any[]) {
      console.log(`🔍 Validando argumentos de ${propertyName}...`);

      switch (validationType) {
        case "positive":
          for (let arg of args) {
            if (typeof arg !== "number" || arg <= 0) {
              throw new Error(
                `❌ ${propertyName}: Se esperaba número positivo, recibió: ${arg}`
              );
            }
          }
          break;
        case "string":
          for (let arg of args) {
            if (typeof arg !== "string") {
              throw new Error(
                `❌ ${propertyName}: Se esperaba string, recibió: ${typeof arg}`
              );
            }
          }
          break;
        case "object":
          for (let arg of args) {
            if (typeof arg !== "object" || arg === null) {
              throw new Error(
                `❌ ${propertyName}: Se esperaba object, recibió: ${arg}`
              );
            }
          }
          break;
      }

      console.log("✅ Validación pasada");
      return originalMethod.apply(this, args);
    };
  };
}

// 4. Decorador de propiedad para solo lectura
function readonly(target: any, propertyName: string) {
  let value: any;

  const getter = function () {
    return value;
  };

  const setter = function (newValue: any) {
    if (value !== undefined) {
      console.log(
        `⚠️  Intento de modificar propiedad readonly: ${propertyName}`
      );
      return;
    }
    value = newValue;
  };

  Object.defineProperty(target, propertyName, {
    get: getter,
    set: setter,
    enumerable: true,
    configurable: true,
  });
}

// ===== APLICACIÓN DE DECORADORES =====

@immutable
class AdvancedCalculator {
  @readonly
  version: string = "1.0.0";

  @readonly
  author: string = "jalcausa";

  private history: string[] = [];

  constructor(public name: string) {
    console.log(`🚀 Creando calculator: ${name}`);
  }

  @logMethod
  @validate("positive")
  add(a: number, b: number): number {
    const result = a + b;
    this.history.push(`${a} + ${b} = ${result}`);
    return result;
  }

  @logMethod
  @validate("positive")
  multiply(a: number, b: number): number {
    const result = a * b;
    this.history.push(`${a} * ${b} = ${result}`);
    return result;
  }

  @logMethod
  divide(a: number, b: number): number {
    if (b === 0) throw new Error("División por cero");
    const result = a / b;
    this.history.push(`${a} / ${b} = ${result}`);
    return result;
  }

  @logMethod
  @validate("string")
  setName(newName: string): void {
    console.log(`Cambiando nombre de ${this.name} a ${newName}`);
    // this.name = newName; // No se puede cambiar porque es immutable
  }

  @logMethod
  getHistory(): string[] {
    return [...this.history]; // Copia para mantener inmutabilidad
  }

  @logMethod
  clearHistory(): void {
    console.log("🗑️  Limpiando historial...");
    this.history.length = 0;
  }
}

// ===== PRUEBAS COMPLETAS =====

console.log("🎯 ===== DEMOSTRACION DE DECORADORES =====\n");

try {
  // Crear instancia
  const calc = new AdvancedCalculator("SuperCalc");
  console.log("\n📊 Estado inicial:");
  console.log("Version:", calc.version);
  console.log("Author:", calc.author);
  console.log("Name:", calc.name);

  console.log("\n🧮 ===== OPERACIONES MATEMÁTICAS =====");

  // Operaciones válidas
  calc.add(10, 5);
  calc.multiply(3, 4);
  calc.divide(20, 4);

  console.log("\n📈 ===== HISTORIAL =====");
  console.log(calc.getHistory());

  console.log("\n🔒 ===== PRUEBAS DE INMUTABILIDAD =====");

  // Intentar cambiar propiedades readonly
  calc.version = "2.0.0"; // No se cambiará
  calc.author = "otro"; // No se cambiará

  console.log("Version después del intento:", calc.version);
  console.log("Author después del intento:", calc.author);

  // Intentar cambiar nombre con validación
  calc.setName("NuevoCalc");

  console.log("\n❌ ===== PRUEBAS DE VALIDACIÓN =====");

  // Estas operaciones fallarán por validación
  try {
    calc.add(-5, 3); // Número negativo
  } catch (error) {
    // ✅ CAMBIO AQUÍ: Type guard para error
    if (error instanceof Error) {
      console.log("Error capturado:", error.message);
    } else {
      console.log("Error capturado:", String(error));
    }
  }

  try {
    calc.setName(123 as any); // No es string
  } catch (error) {
    // ✅ CAMBIO AQUÍ: Type guard para error
    if (error instanceof Error) {
      console.log("Error capturado:", error.message);
    } else {
      console.log("Error capturado:", String(error));
    }
  }

  console.log("\n🗑️ ===== LIMPIEZA =====");
  calc.clearHistory();
  console.log("Historial después de limpiar:", calc.getHistory());
} catch (error) {
  // ✅ CAMBIO AQUÍ: Type guard para error general
  if (error instanceof Error) {
    console.log("Error general:", error.message);
  } else {
    console.log("Error general:", String(error));
  }
}

console.log("\n🎯 ===== FIN DE LA DEMOSTRACION =====");
