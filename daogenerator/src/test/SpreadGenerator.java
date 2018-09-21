package test;


import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

/**
 * 自动生成GreenDao Entity
 */
public class SpreadGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "mail139.launcher.bean");

        Entity wordEntity = schema.addEntity("SpreadInfo");
        wordEntity.implementsInterface("Parcelable");
        wordEntity.setJavaDoc("write");
        wordEntity.addIdProperty().javaDocField("id").primaryKey().autoincrement();
        wordEntity.addStringProperty("passid").javaDocField("AccountInfo passid");
        wordEntity.addStringProperty("name").javaDocField("推广名字");
        wordEntity.addStringProperty("imageUrl").javaDocField("图标地址");
        wordEntity.addStringProperty("prourl").javaDocField("推广链接");
        wordEntity.addStringProperty("idNum").javaDocField("id文本");

        String classPath = SpreadGenerator.class.getResource("").toString();
        String appBasePath = "app/src/main/java/";
        String path = classPath.substring(classPath.lastIndexOf(':') + 1, classPath.indexOf("daogenerator")) + appBasePath;
        System.out.println("path:" + path);
        new DaoGenerator().generateAll(schema, path);
    }
}
