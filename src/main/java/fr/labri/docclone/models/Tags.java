package fr.labri.docclone.models;

import fr.labri.docclone.algo.ElementWithIdentifier;
import fr.labri.docclone.algo.FcaElement;

import java.util.Set;

/**
 * Created by oumaziz on 02/03/2016.
 */
public class Tags extends ElementWithIdentifier {

    private final Set<FcaElement> tags;

    public Tags(Set<FcaElement>  tags) {
       this.tags = tags;
    }

    @Override
    public String getIdentifier() {
        StringBuilder sb = new StringBuilder();

        for (FcaElement t : tags)
            sb.append(t.getIdentifier());

        return sb.toString();
    }

    @Override
    public int hashCode() {
        return this.tags.hashCode();
    }

    public Set<FcaElement> getTags(){
        return this.tags;
    }
}
