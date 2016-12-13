package controllers;

import org.json.JSONArray;
import play.mvc.Controller;
import play.mvc.Result;
import util.GeneratorPseudo;
import util.Http;
import views.html.*;


/**
 * Par Alexis le 26/11/2016.
 */

public class GeneratePseudoController extends Controller
{

    public GeneratePseudoController()
    {}

    @Http.Get(desc = "Generate a pseudonyme")
    public Result generate(int nb, int syl)
    {
        if(nb > 10000)
            nb = 10000;
        GeneratorPseudo g = new GeneratorPseudo();
        JSONArray ja = new JSONArray();

        for (int i = 0; i < nb + 1; i++)
            ja.put(g.generate(syl));

        return ok(ja.toString());
    }

    @Http.Get
    public  Result generatePage()
    {
        return ok(pseudoGen.render());
    }


}
