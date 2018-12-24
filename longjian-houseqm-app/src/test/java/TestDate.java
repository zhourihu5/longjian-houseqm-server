import com.longfor.longjian.houseqm.util.DateUtil;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Houyan
 * @date 2018/12/22 0022 14:05
 */
public class TestDate {
    public static void main(String[] args) {
        Date date1 = new Date();
        Calendar c = Calendar.getInstance();
        Date date= DateUtil.timeStampToDate((int) (date1.getTime()/1000), "yyyy-MM-dd");
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, 1);
        Date endOn = DateUtil.timeStampToDate((int) (c.getTime().getTime()/1000), "yyyy-MM-dd");
        System.out.println(date1);
        System.out.println(endOn);
    }
}
