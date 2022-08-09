# Informe Lab2: Programación Orientada a Objetos con Scala

## Grupo 20

Integrantes:
* Nazareno Gerbaldo
* Fernando Farías
* Gonzalo Zigarán

## Respuestas

### Parte 1

#### 1. ¿Como es la interfaz de la clase u objeto que implementaron para el primer componente?

Para la primer componente implementamos una clase abstracta `Parser` que tiene las funciones comunes que debe hacer parser, y luego 2 clases que heredan de esta, `RSSParser` y `RedditParser` que tienen la implementación específica de cómo procesar el texto para el caso específico de como vienen los datos. 

#### 2. ¿Utilizaron una clase o un objeto? ¿Por qué?

Implementamos clases para los Parser, para luego porder instanciarlos y crear objetos especificos para parsear cada url.

### Parte 2

#### 1. ¿Por qué utilizar literales como Strings para diferenciar comportamientos similares crea acoplamiento? 

Uno de los problemas que pueden surgir al utilizar literales para diferenciar comportamientos aparecería al extender el código para poder parsear nuevos formatos. Si utilizácemos literales, deberíamos entrar a nuestra clase `Parser` y crear una nueva comparación que nos permitiese tratar de forma diferenciada el nuevo formato, mientras que haciendo uso de la *herencia*, se debería agregar una nueva clase *hijo* la cual no cambiaría en nada las ya existentes. De esta forma, en un caso tenemos un acoplamiento indeseado mientras que de la otra forma lo evitamos.

#### 2. ¿Qué concepto(s) de la Programación Orientada a Objetos utilizaron para resolver este problema?

En el libro *John C. Mitchell, Concepts in Programming Languages (2002)*, se mencionan cuatro conceptos fundamentales en la programación orientada a objetos. Describimos resumidamente como estas ideas fueron utilizadas en nuestra implementación.

- ***Dynamic lookup**: when a message is sent to an object, the function code to be executed is determined by the way that the object is implemented.* En nuestra implementación, por ejemplo, al ejecutar el método `processText()` sobre un objeto, el código ejecutado va a depender de si el objeto es una instancia de `RSSParser` o `RedditParser`. 
- ***Abstraccion**: the implementation details are hidden inside a program unit with a specific interface.* La forma de acceder a las funcionalidades de las instancias de nuestras clases es a través de sus métodos.
- ***Subtyping**: if some object `a` has all of the functionality of another object `b`, then we may use `a` in any context expecting `b`.* En `FeedService` para suscribir una urls, se espera que se la acompañe con un objeto tipo `Parser`, pero como `RSSParser` y `RedditParser` tienen todas las funcionalidades de `Parser`, se pueden pasar como argumentos.
- ***Inheritance**: is the ability to reuse the definition of one kind of object to define another kind of object.* Las clases `RSSParser` y `RedditParser` heredan funcionalidades de la clase `Parser`.

Otros conceptos que tuvimos en cuenta a la hora de implementar nuestro código fueron los de acoplamiento, encapsulación, y polimorfia.


#### [Op] ¿Qué ventajas tiene definir una case class para extraer el contenido de un archivo json?

La principal ventaja que encontramos (y utilizamos en la implementación del *"main"*), es que nos permite pensar a los objetos *json* como tuplas con campos a los cuales se pueden acceder mediante el operador `.`. 


### Parte 3

#### 1. ¿Qué clase almacena las URLs?

Las URLs se van almacenando en la clase `FeedService`, dentro del atributo `urls`, que es una `MutableList` que tiene pares de `(String, Parser)`, donde el primer argumento es el string, y el segundo es el parser a utilizar para esa url. 

#### 2. ¿Cómo funciona el polimorfismo de clases en su implementación? ¿Qué desventaja tiene pasar al método subscribe un parámetro de tipo string que pueda tomar los valores “rss” y “reddit”, y dejar que decida qué tipo de parser usar?

En nuestra implementación, la *Polimorfia* se puede ver claramente en el hecho que a objetos del tipo `RSSParser` o `RedditParser` se les pueden aplicar los mismos métodos.
En caso de que al método `subscribe` le pasásemos una parámetro del tipo *String* (con valores *rss* o *reddit*) y en base a esto decida qué **parser** usar, estaríamos teniendo un conflicto parecido al descripto en la primer pregunta de la **Parte 2**. Deberíamos cambiar constantemente nuestra clase a medida que quisieramos agregar nuevos formatos a *parsear*. Por eso, solucionamos dicho problema haciendo que nuestra implementación del método tomara como parámetro un *objeto* del tipo `Parser`.


## Decisiones de diseño

Dos desiciones que nos parece importante mencionar son las siguientes. En primer lugar, para tener una mejor modularización de nuestro código, declaramos nuestra clase `Parser` y quienes heredan de esta en un documento y en otro la clase `FeedService`. A su vez, cada uno de estos lo ubicamos dentro de carpetas diferentes como, desde la cátedra, se hizo con el módulo `nermodel`. Sumando así los módulos `service` y `parser`.
En segundo lugar, agregamos una serie de pálabras a `stopwords` que no estaban incluídas originalmente. Estas fueron contracciones del tipo *I've* o *I'm*.


## **Punto estrella:** Mejor modelo

Para realizar esta mejora, se creo un nuevo modelo dentro del módulo `nermodel` que se nombró `NERNormalizedModel`, que está basado en el modelo anterior (`NERSimpleModel`), pero modificandole la manera de contar las entidades, para incorporar la cuenta total de entidades para cada artículo, y así poder ir normalizando por articulo las entidades. Luego se suman todas estás normas para poder ordenarlas de acuerdo a este valor. Claramente toda la responsabilidad de realizar esta mejora está en el modelo, y la decisión de crear un modelo nuevo es para no modificar lo ya realizado, solo se modifica el `case class NERCount`, agregandole un atributo con un valor por defecto para no tener que modificar en nada el modelo anterior. El main solo se modifica lo mínimo para que tome el nuevo modelo e imprima los nuevos valores generados. 