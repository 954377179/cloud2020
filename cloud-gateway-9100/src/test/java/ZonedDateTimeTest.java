import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author wck
 * @date 2020/4/10
 **/
public class ZonedDateTimeTest {
    public static void main(String[] args) {
        // 用默认时区获取当前时间
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        System.out.println(zonedDateTime);

        // 用指定时区获取当前时间
        ZonedDateTime zonedDateTime1 = ZonedDateTime.now(ZoneId.of("America/New_York"));
        System.out.println(zonedDateTime1);
    }
}
