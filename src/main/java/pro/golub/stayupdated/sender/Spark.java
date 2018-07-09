package pro.golub.stayupdated.sender;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;
import twitter4j.Status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Spark {

    private static Set<String> hashTags = new HashSet<>(Arrays.asList("java", "blockchain", "infosec"));

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setAppName("stayupdated").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        hashTags.forEach(hashTag -> {
            //TODO mapreduce?
            JavaRDD<Status> javaRDD = sc.objectFile("result/twitter_" + hashTag + "_" + LocalDate.now().minusDays(1).toString());
            javaRDD.map(Status::getText).collect().forEach(System.out::println);
        });

    }

}
