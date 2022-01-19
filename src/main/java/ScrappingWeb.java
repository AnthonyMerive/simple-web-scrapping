
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ScrappingWeb {



    public static Document getHTML(String url) {
        try {

            return Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(100000)
                    .get();

        } catch (Exception e) {
            System.out.println("Error al obtener el codigo HTML");
            return null;
        }

    }

    public List<MovieEntity> scrapping() {

        List data = new ArrayList();

        Elements movies = ScrappingWeb.getHTML("https://pelis24.app/")
                .select("div#topview-today")
                .select("div#content-box")
                .select("div.ml-item");

        for (Element movie : movies) {

            MovieEntity movieEntity = new MovieEntity();

            try {
                String urlMovie = movie.select("div.ml-item")
                        .select("a")
                        .attr("href");

                Document htmlMovie = ScrappingWeb.getHTML(urlMovie);

                assert htmlMovie != null;

                movieEntity.setTitle(htmlMovie
                        .select("h3[itemprop=name]").text());

                movieEntity.setDescription(htmlMovie
                        .select("div.mvic-desc")
                        .select("p")
                        .text());

                movieEntity.setImage( htmlMovie
                        .select("img.hidden")
                        .attr("src"));

                movieEntity.setVideoUrl(htmlMovie
                        .select("div.movieplay")
                        .select("iframe").attr("src"));

                data.add(movieEntity);

            } catch (Exception e) {
                System.out.println("Error al entrar en la pelicula");
            }

        }

        return data;
    }

    public static void main(String[] args) {

        List movies = new ScrappingWeb().scrapping();

        System.out.println(movies);
    }
}
