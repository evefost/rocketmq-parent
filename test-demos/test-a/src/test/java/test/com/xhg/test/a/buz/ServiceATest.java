package test.com.xhg.test.a.buz;

import com.xie.message.client.pojo.MessageWrapper;
import com.xie.test.a.ApplicationA;
import com.xie.test.a.buz.ServiceAServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** 
* ServiceA Tester. 
* 
* @author <Authors name> 
* @since <pre>���� 12, 2018</pre> 
* @version 1.0 
*/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationA.class)
public class ServiceATest { 


    @Autowired
    private ServiceAServiceImpl serviceA;


/** 
* 
* Method: doAdd(String aaa) 
* 
*/ 
@Test
public void testDoAdd() throws Exception {
    //serviceA.add("fdfdf");
    System.out.println("fdfd");
}

    @Test
    public void testUpdateTransactionDataStatus() throws Exception {
        MessageWrapper messageWraper  = new MessageWrapper();
        messageWraper.setMsgId("a29bb53e-b44d-4736-9c71-00b37dc5ac0a");
        ///messageWraper.setStatus(4);

    }

} 
