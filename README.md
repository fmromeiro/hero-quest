# mc322-hero-quest :D

## Estrutura de módulos

- renderer
- controller
    - Responsável por organizar as chamadas de método às entities e executar funções fora dos escopos dessas.
    - Eventualmente pode ter que controlar eventos (para controlar efeitos de poções e armadilhas de veneno)
- entities
- input
- ai
- auxiliars
- items
    - durable (armaduras e tal)
    - consumable (poções. magias e tal)

---

Fyvon0 - For future reference, I just noticed I was doing a lot of copies between internals and I now repent for my sins.
Please friends, I ask you dearly to please not create useless copies of objects, they are smart enough to handle themselves.
