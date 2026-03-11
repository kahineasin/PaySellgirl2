//package org.sellgirlPayHelperNotSpring;
//
//import junit.framework.TestCase;
//import org.apache.avro.file.DataFileWriter;
//import org.apache.avro.io.DatumWriter;
//import org.apache.avro.specific.SpecificDatumWriter;
//import org.sellgirlPayHelperNotSpring.model.PFConfigTestMapper;
//import com.sellgirl.pfHelperNotSpring.config.PFAppConfig;
//import com.sellgirl.pfHelperNotSpring.config.PFDataHelper;
//
//import java.io.File;
//
//public class UncheckAvro001 extends TestCase {
//    public static void initPFHelper() {
//        PFDataHelper.SetConfigMapper(new PFConfigTestMapper());
//        new PFDataHelper(new PFAppConfig());
//    }
//    public void testXx() throws Exception{
//        initPFHelper();
//        //...
//        User user1 = new User();
//        user1.setName("Alyssa");
//        user1.setFavoriteNumber(256);
//// Leave favorite color null
//
//// Alternate constructor
//        User user2 = new User("Ben", 7, "red");
//
//// Construct via builder
//        User user3 = User.newBuilder()
//                .setName("Charlie")
//                .setFavoriteColor("blue")
//                .setFavoriteNumber(null)
//                .build();
//
//        // Serialize user1, user2 and user3 to disk
//        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<User>(User.class);
//        DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(userDatumWriter);
//        dataFileWriter.create(user1.getSchema(), new File("users.avro"));
//        dataFileWriter.append(user1);
//        dataFileWriter.append(user2);
//        dataFileWriter.append(user3);
//        dataFileWriter.close();
//
//    }
//}
