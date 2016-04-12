package fr.labri.docclone.models;

/**
 * Created by oumaziz on 02/03/2016.
 */
public class Method extends ElementWithIdentifier {

    private String file;
    private String signature;

    public Method(String file, String signature) {
        this.signature = signature;
        this.file = file;
    }

    @Override
    public String getIdentifier() {
        return file + "#" + signature;
    }
}
