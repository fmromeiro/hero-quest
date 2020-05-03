package br.ic.unicamp.mc322.heroquest.entities;

public class StatusModifier { //Isso aqui vai ser usado em outro lugar fora de Character? Senão, pode ser vísivel só por lá.
    private final Character.Attribute attribute;
    private final int modifier; //TODO: assume que o modificador é imutável. Caso existam modificadores mutáveis,
    // pode-se deletar esse StatusModifier e criar um outro ou alterar essa classe para que o modificador sejá mutável

    public StatusModifier(Character.Attribute attribute, int modifier) {
        this.attribute = attribute;
        this.modifier = modifier;
    }

    public Character.Attribute getAttribute() { return this.attribute; }

    public int getModifier() { return this.modifier; }
}
