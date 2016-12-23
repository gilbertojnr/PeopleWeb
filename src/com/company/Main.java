package com.company;

import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.List;
public class Main {
    public static   ArrayList<People> person = new ArrayList<>();


    public static void main(String[] args) throws Exception {
        File f = new File("people.csv");
        Scanner fileScanner = new Scanner(f);
        while (fileScanner.hasNext()) {
            String line = fileScanner.nextLine();
            while(line.startsWith("id, first_name")){
                line = fileScanner.nextLine();
            }
            String[] columns = line.split(",");
            People names = new People(columns[0], columns[1], columns[2], columns[3], columns[4], columns[5]);
            person.add(names);

        }
        fileScanner.close();


        Spark.init();
        Spark.get("/", ((request, response) -> {
                    String offsetString = request.queryParams("offset");
                    if (offsetString == null) {
                        offsetString = "0";
                    }
                    int offset = Integer.parseInt(offsetString);

                    List<People> personSubList = new ArrayList<>();
                    personSubList = person.subList(offset, offset + 20);

                    HashMap m = new HashMap();
                    m.put("People", personSubList);
                    m.put("nextOffset", offset +20);
                    m.put("prevOffset", offset -20);

                    return new ModelAndView(m, "people.html");


                }),
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/person",
                ((request, response) -> {
                    String id = request.queryParams("id");
                    int idNum = Integer.parseInt(id);
                    HashMap m = new HashMap();
                    m.put("id", person.get(idNum -1));
                    return new ModelAndView(m, "person.html");


                }),
                new MustacheTemplateEngine()
        );


    }
}
