package fr.labri.docclone.models;

import fr.labri.docclone.algo.ElementWithIdentifier;

/**
 * Created by oumaziz on 02/03/2016.
 */
public class Tag extends ElementWithIdentifier {

    private String name;
    private String value;

    public Tag(String name, String value) {
        this.name = name;
        this.value = cleanString(value);
    }

    private String cleanString(String dirty){
        return dirty.replaceAll("\\p{C}", "")
                    .replaceAll("[\\\\\"]", " ")
                    .replaceAll("(\\s{2,})", " ")
                    .trim().toLowerCase();
    }

    @Override
    public String getIdentifier() {
        return name + " " + value;
    }
}
