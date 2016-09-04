package test;


import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

/**
 * 自动生成GreenDao Entity
 */
public class WordGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.qwwuyu.recite.bean");

        Entity wordEntity = schema.addEntity("Word");
        wordEntity.implementsInterface("Parcelable");
        wordEntity.setJavaDoc("Update by qw on 2016/8/22.");
        wordEntity.addIdProperty().notNull().javaDocField("id").primaryKey().autoincrement();
        wordEntity.addStringProperty("text").notNull().javaDocField("单词文本");
        wordEntity.addStringProperty("accents").javaDocField("音标");
        wordEntity.addStringProperty("content").javaDocField("单词介绍");
        wordEntity.addIntProperty("index").javaDocField("乱序角标").notNull();
        wordEntity.addBooleanProperty("collect").javaDocField("收藏").notNull();
        wordEntity.addLongProperty("collectTime").javaDocField("收藏时间").notNull();

        String classPath = WordGenerator.class.getResource("").toString();
        String appBasePath = "app/src/main/java/";
        String path = classPath.substring(classPath.lastIndexOf(':') + 1, classPath.indexOf("daogenerator")) + appBasePath;
        System.out.println("path:" + path);
        new DaoGenerator().generateAll(schema, path);
    }
}
