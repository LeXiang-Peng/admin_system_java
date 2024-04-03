package com.plx.admin_system.utils.pojo.schduledCourse;

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
    private Integer currentTimes;
    private Integer totalTimes;
    /**
     * 0 - 周一 ...4 - 周五
     */
    private Integer weekDay;
    /**
     * 0 - 一二大节 ... 4 - 七八大节
     */
    private Integer courseTime;
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
        this.currentTimes = task.getCurrentTimes();
        this.totalTimes = task.getTotalTimes();
        this.weekDay = task.getWeekDay();
        this.courseTime = task.getCourseTime();
        this.classroom = task.getClassroom();
    }

    /**
     * init 初始化
     *
     * @param classroomListSize
     */
    public void init(Integer classroomListSize) {
        this.classroom = new Random().nextInt(classroomListSize);
        this.weekDay = new Random().nextInt(5);
        this.courseTime = new Random().nextInt(4);
    }

    /**
     * same course during same day 同一天有两门相同的课程
     *
     * @param task
     * @return
     */
    public Boolean SameCourseDuringSameDay(CourseTask task) {
        if (this.courseOverlaps(task) && duringSameDay(task)) {
            return true;
        }
        return false;
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
                && Objects.equals(this.courseTime, task.getCourseTime())) {
            return true;
        }
        return false;
    }

    /**
     * during same day 同一天
     *
     * @param task
     * @return
     */
    private Boolean duringSameDay(CourseTask task) {
        if (Objects.equals(this.weekDay, task.weekDay)) {
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
        for (String cur_clazz : this.course.getClazzList()) {
            for (String task_clazz : task.course.getClazzList()) {
                if (Objects.equals(cur_clazz, task_clazz)) {
                    return true;
                }
            }
        }
        return false;
    }
}
