# Relatório

Nesse relatório justificamos decisões de implementação tomadas em nosso programa.

## Índice

- [Implementação](#implementao)
    - [Salas](#salas)

## Implementação

### Salas

Num primeiro momento, havíamos decidido por criar uma classe `Room` que representava uma sala convexa e uma classe `Corridor`, que herdaria de `Room` e teria uma lista de `Room`, já que corredores podem ser côncavos. Essas classes mantinham uma lista de todas as entidades que estavam numa sala a qualquer dado momento. Nessa implementação, as paredes não ocupariam espaço no mapa, se mantendo fiel ao mapa original de _Hero's Quest_, visto abaixo:

![Mapa do Hero Quest][hero-quest-map]

Porém, encontramos dificuldades implementando paredes e portas que não ocupavam espaço, principalmente quando fomos implementar passagem de uma sala para outra e linha de visão. Como a especificação não exige que esse aspecto do mapa original seja preservado, optamos por desenvolver salas com paredes e portas que ocupam espaço no mapa.

Nesse novo sistema, se quiséssemos fazer uma sala 2x2, por exemplo, ela teria que ser 4x4, pois a primeira e a última linha e a primeira e a última coluna serão paredes.

Para implementar a linha de visão, usamos o [Algoritmo de Bresenham](https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm) para detectar o que é visível para o jogador.

[hero-quest-map]: https://i.imgur.com/Glt9wux.png
[bresenham-line-algorithm]: https://en.wikipedia.org/wiki/Bresenham%27s_line_algorithm