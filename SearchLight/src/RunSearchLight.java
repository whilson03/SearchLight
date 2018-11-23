import java.io.File;
import java.io.IOException;

public class RunSearchLight {
    public static void main(String[] args) throws IOException {
        SearchLight search = new SearchLight(new File("/Users/mac/Movies/movies/"));
        // conduct a general search
       search.generalSearch();
    }
}
