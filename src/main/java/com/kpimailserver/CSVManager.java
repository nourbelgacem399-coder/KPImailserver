package com.kpimailserver;

import java.io.*;
import java.util.ArrayList;

public class CSVManager {


    public void save(ArrayList<KPI> data, String fileName) {


        try {


            PrintWriter writer = new PrintWriter(new FileWriter(fileName));



            writer.println("Date,Hour,MailRelayed,Spam,Virus");


            for (KPI kpi : data) {


                writer.println(
                        kpi.getDate() + "," +
                                kpi.getHour() + "," +
                                kpi.getMailRelayed() + "," +
                                kpi.getSpam() + "," +
                                kpi.getVirus()
                );


            }


            writer.close();


            System.out.println("CSV créé avec succès !");


        } catch(Exception e) {


            System.out.println("Erreur CSV : " + e.getMessage());


        }

    }

}