package fr.labri.docclone;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import fr.labri.docclone.colibri.lib.Concept;
import fr.labri.docclone.colibri.lib.Relation;
import fr.labri.docclone.builder.ContextBuilder;
import fr.labri.docclone.colibri.lib.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by oumaziz on 01/03/2016.
 */
public class App {

    public static void main(String[] args){
        run(args);
    }

    private static void run(String[] args){
        if((args.length >= 2) && (args[0].equals("-f") && args[1] != null)) {
            String filename = args[1];
            String fileoutput = args[1].split("\\.")[0] + "_clones.json";

            try {
                try {

                    JsonObject result = new JsonParser().parse(new FileReader(filename)).getAsJsonObject();
                    ContextBuilder cb = new ContextBuilder(result);

                    Relation rel = new TreeRelation();

                    cb.generateFromMethods(rel);
                    //cb.generateFromClasses(rel);
                    //cb.generateFromAttributes(rel);

                    Lattice lattice = new HybridLattice(rel);

                    Iterator it = lattice.conceptIterator(Traversal.TOP_OBJSIZE);

                    ArrayList<Concept> clones = new ArrayList<>();

                    while (it.hasNext()) {
                        Concept c = (Concept) it.next();
                        if (c.getObjects().size() >= 2) {
                            if(c.getAttributes().size() > 0){
                                clones.add(c);
                            }
                        }
                    }

                    System.out.println("Clones count : " + clones.size());


                    String json = new Gson().toJson(clones);

                    try {

                        File myFile = new File(fileoutput);
                        myFile.createNewFile();
                        FileOutputStream fOut = new FileOutputStream(myFile);
                        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                        myOutWriter.append(json);
                        myOutWriter.close();
                        fOut.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (JsonSyntaxException e) {
                    System.err.println("Error : Incorrect JSON file.");
                }
            } catch (FileNotFoundException e) {
                System.err.println("Error : File not found.");
            }
        }else{
            System.err.println("Error : Please specify a source file.");
        }
    }
}
