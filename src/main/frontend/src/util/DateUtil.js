
class DateUtil {

    static weekDay = ["일", "월", "화", "수", "목", "금", "토"];


    //
    static compareDate(date1, date2) {
        if (date1.getFullYear() !== date2.getFullYear()) {
            return date1.getFullYear() - date2.getFullYear();
        } else if (date1.getMonth() !== date2.getMonth()) {
            return date1.getMonth() - date2.getMonth();
        } else {
            return date1.getDate() - date2.getDate();
        }
    }

    static getDateKr(date) {
        return (date.getMonth() + 1) + "월 " + date.getDate()+"일 "+ this.weekDay[date.getDay()] + "요일";
    }

    static getDateInMessage(date) {
        let hour = date.getHours();
        let minute = date.getMinutes();
        let ampm = hour >= 12 ? "오후" : "오전";
        hour = hour % 12;
        return ampm + " " + hour + ":" + minute;
    }
}

export default DateUtil;