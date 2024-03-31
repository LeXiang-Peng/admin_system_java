package com.plx.admin_system.utils.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.Random;

/**
 * @author plx
 */
@Data
@NoArgsConstructor
public class CourseTask {
    /**
     * 中用做数据库查询时分组的唯一标识 —— 在数据库中代表课程编号(course_id)
     */
    private Integer id;
    private SchedulingCourse course;
    private Integer weeksTotal;
    private Integer timesOnceAWeek;
    private Integer currentTime;
    /**
     * 0 - 周一 ...4 - 周五
     */
    private Integer weekDay;
    /**
     * 0 - 一二大节 ... 4 - 七八大节
     */
    private Integer course_time;
    /**
     * 按List下标编码
     * 值为0，代表 List索引为0 的教室
     */
    private Integer classroom;

    public CourseTask(CourseTask task) {
        this.id = task.id;
        this.course = new SchedulingCourse(task.getCourse());
        this.weeksTotal = task.getWeeksTotal();
        this.timesOnceAWeek = task.getTimesOnceAWeek();
        this.currentTime = task.getCurrentTime();
    }

    /**
     * init 初始化
     *
     * @param classroomListSize
     */
    public void init(Integer classroomListSize) {
        Random random = new Random();
        this.classroom = random.nextInt(classroomListSize);
        this.weekDay = random.nextInt(5);
        this.course_time = random.nextInt(4);
    }

    /**
     * 同一时间（weekDay && course_time 相等）
     * 同一课程
     *
     * @param task
     * @return
     */

    public Boolean courseOverlapsDuringSameTime(CourseTask task) {
        if (this.duringSameTime(task) && this.courseOverlaps(task)) {
            return true;
        }
        return false;
    }

    /**
     * 同一时间（weekDay && course_time 相等）
     * 同一班级
     *
     * @param task
     * @return
     */
    public Boolean clazzOverlapsDuringSameTime(CourseTask task) {
        if (this.duringSameTime(task) && this.clazzOverlaps(task)) {
            return true;
        }
        return false;
    }

    /**
     * 同一时间（weekDay && course_time 相等）
     * 同一讲师
     *
     * @param task
     * @return
     */
    public Boolean lecturerOverlapsDuringSameTime(CourseTask task) {
        if (this.duringSameTime(task) && lecturerOverlaps(task)) {
            return true;
        }
        return false;
    }

    /**
     * during same time 同一时间
     *
     * @param task
     * @return
     */
    private Boolean duringSameTime(CourseTask task) {
        if (Objects.equals(this.weekDay, task.getWeekDay())
                && Objects.equals(this.course_time, task.getCourse_time())) {
            return true;
        }
        return false;
    }

    /**
     * student total overflows 学生人数是否超过教室最大容量
     *
     * @return
     */
    public Boolean studentTotalOverflows() {
        if (this.course.getStudentTotal() == 0) {
            return true;
        }
        return false;
    }

    /**
     * course overlaps 存在课程重叠
     *
     * @param task
     * @return
     */
    private Boolean courseOverlaps(CourseTask task) {
        if (Objects.equals(this.id, task.getId())) {
            return true;
        }
        return false;
    }

    /**
     * lecturer overlaps 教师是否在重叠
     *
     * @return
     */
    private Boolean lecturerOverlaps(CourseTask task) {
        if (Objects.equals(this.course.getLecturerId(), task.getCourse().getLecturerId())) {
            return true;
        }
        return false;
    }

    /**
     * clazz overlaps 班级是否重叠
     *
     * @param task
     * @return
     */
    private Boolean clazzOverlaps(CourseTask task) {
        if (Objects.equals(this.course.getClazzList(), task.getCourse().getClazzList())) {
            return true;
        }
        return false;
    }
}
