package test.com.xhg.test.a.buz;

import com.xie.message.client.pojo.MessageWrapper;
import com.xie.message.client.pojo.TargetEvent;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class MainTest {
    public static void main(String[] args) {

        TargetEvent targetEvent = new TargetEvent(new MainTest(),new MessageWrapper());
        targetEvent.setTopic("xhg-device-service-topic");
        targetEvent.setTag("DeviceEvent");
        String condition = "#event.topic=='xhg-device-service-topic' && #event.tag=='DeviceEvent'";
        ExpressionParser expressionParser = new SpelExpressionParser();

        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("event", targetEvent);
        System.out.println(expressionParser.parseExpression("#event.topic").getValue(context));
//        System.out.println("[SpELTest - testParse ] {} "+ expression.getValue(context));

        //获取方法参数名
//        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
//        for (Method method : new StringUtils().getClass().getDeclaredMethods()) {
//            for (String s : discoverer.getParameterNames(method)) {
//                System.out.print("parm: "+s+"  ");
//            }
//            System.out.println("methodName:  "+method.getName());
//        }


    }
}
