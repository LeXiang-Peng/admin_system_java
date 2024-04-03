package com.plx.admin_system.utils;

import com.plx.admin_system.utils.pojo.schduledCourse.CourseTask;

import java.util.*;

/**
 * @author plx
 */
public class GeneticAlgorithm {
    /**
     * 种群的规模（0-100） population size
     */
    private static final Integer POPULATION_SIZE = 32;
    /**
     * 种群的变异概率 mutation probability
     */
    private static final Double MUTATION_PROBABILITY = 0.3;
    /**
     * 精英种群的个数 elite size
     */
    private static final Integer ELITE_SIZE = 15;
    /**
     * 进化代数（100-500） max iteration
     */
    private static final Integer MAX_ITERATION = 500;
    /**
     * 所有的种群 种群中 存放每一个 **需要编排的课程列表** (个体)
     */
    private List<List<CourseTask>> population;


    public List<CourseTask> evolute(List<CourseTask> tasks, Integer classroomListSize) {
        //初始化
        this._init_population(tasks, classroomListSize);
        //如果无法满足当前_conflicts = 0,即完美解不存在(或者本次迭代中未找到), 则返回备选集最优解
        LinkedHashMap<List<CourseTask>, Integer> res = new LinkedHashMap<>();
        //迭代 —— 通过变异交叉获取可能的解集，接着验证解集的适应度分值，当碰撞分值为0，则解已经出现
        for (int i = 0; i < MAX_ITERATION; i++) {
            List<List<CourseTask>> newPopulation = new ArrayList<>();
            LinkedHashMap<List<CourseTask>, List<Integer>> rateMap = _rate(population, ELITE_SIZE, tasks.size());
            //遍历适应度分值，查看是否出现解
            for (List<CourseTask> key : rateMap.keySet()) {
                //出现解，即conflicts（必须达成的条件）= 0,
                if (Objects.equals(rateMap.get(key).get(0), 0)) {
                    //将满足必要条件的结果集存下来，作为备选集（用作保底）
                    List<CourseTask> res_data = new ArrayList<>();
                    for (CourseTask temp : key) {
                        res_data.add(new CourseTask(temp));
                    }
                    res.put(res_data, rateMap.get(key).get(1));
                    //尽量返回完美解
                    if (Objects.equals(rateMap.get(key).get(1), 0)) {
                        return key;
                    }
                }
            }
            //精英种群
            for (List<CourseTask> key : rateMap.keySet()) {
                newPopulation.add(key);
            }
            while (newPopulation.size() < POPULATION_SIZE) {
                if (Math.random() < MUTATION_PROBABILITY) {
                    //将变异个体加入种群中
                    newPopulation.add(_mutate(newPopulation, classroomListSize));
                } else {
                    //通过两个精英个体进行交叉
                    _crossOver(newPopulation);
                }
            }
            this.population = newPopulation;
        }
        //当完美解不存在时，返回最优解
        return getBestIndividual(res);
    }

    /**
     * get best individual 获取备选集中的最优解
     *
     * @param res
     * @return
     */
    List<CourseTask> getBestIndividual(Map<List<CourseTask>, Integer> res) {
        List<Map.Entry<List<CourseTask>, Integer>> list = new ArrayList<>(res.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<List<CourseTask>, Integer>>() {
            @Override
            public int compare(Map.Entry<List<CourseTask>, Integer> o1, Map.Entry<List<CourseTask>, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        return res.entrySet().iterator().next().getKey();
    }

    /**
     * 初始化
     *
     * @param tasks
     * @param classroomListSize
     */
    private void _init_population(List<CourseTask> tasks, Integer classroomListSize) {
        this.population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            List<CourseTask> individual = new ArrayList<>();
            for (int j = 0; j < tasks.size(); j++) {
                CourseTask temp = tasks.get(j);
                temp.init(classroomListSize);
                individual.add(new CourseTask(temp));
            }
            population.add(individual);
        }
    }

    /**
     * 计算适应度分值
     *
     * @return
     */
    private LinkedHashMap<List<CourseTask>, List<Integer>> _rate(List<List<CourseTask>> population, Integer elite, Integer courseSize) {
        LinkedHashMap<List<CourseTask>, List<Integer>> temp_res = new LinkedHashMap();
        LinkedHashMap<List<CourseTask>, List<Integer>> res = new LinkedHashMap();
        CourseTask gene;
        CourseTask _gene;
        for (List<CourseTask> individual : population) {
            /**
             * 防止在同一天或者同一个时段的课过于密集
             * 记录横向和纵向的课程数（横向 —— 时间段/纵向 —— 每天）
             * 用于后续非必要条件冲突值计算
             */
            int[] weekDayArr = new int[5];
            int[] courseTimeArr = new int[4];
            /**
             * 碰撞冲突值（分 必要条件和非必要条件——conflicts(必要条件的冲突值),_conflicts(非必要条件的冲突值)）
             */
            List<Integer> conflictList = new ArrayList<>();
            //必须达成的条件，即值必须为0
            int conflicts = 0;
            //非必须，优中选优
            int _conflicts = 0;
            Integer weekDay = individual.get(courseSize - 1).getWeekDay();
            Integer courseTime = individual.get(courseSize - 1).getCourseTime();
            /**
             * 记录下 每天的课程数 和 每个时间段的课程数
             */
            weekDayArr[weekDay] += 1;
            courseTimeArr[courseTime] += 1;
            if (individual.get(courseSize - 1).studentTotalOverflows()) {
                conflicts++;
            }
            for (int i = 0; i < courseSize - 1; i++) {
                gene = individual.get(i);
                weekDayArr[gene.getWeekDay()] += 1;
                courseTimeArr[gene.getCourseTime()] += 1;
                /**
                 * 如下见知意
                 */
                if (gene.studentTotalOverflows()) {
                    conflicts++;
                }
                for (int j = i + 1; j < courseSize; j++) {
                    _gene = individual.get(j);
                    if (gene.courseOverlapsDuringSameTime(_gene)) {
                        conflicts++;
                    }
                    if (gene.clazzOverlapsDuringSameTime(_gene)) {
                        conflicts++;
                    }
                    if (gene.lecturerOverlapsDuringSameTime(_gene)) {
                        conflicts++;
                    }
                    if (gene.SameCourseDuringSameDay(_gene)) {
                        conflicts++;
                    }
                }
            }
            /**
             * 计算非必要条件的冲突
             */
            _conflicts = get_conflicts(weekDayArr, courseTimeArr);
            conflictList.add(conflicts);
            conflictList.add((_conflicts));
            temp_res.put(individual, conflictList);
        }
        List<Map.Entry<List<CourseTask>, List<Integer>>> list = new ArrayList<>(temp_res.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<List<CourseTask>, List<Integer>>>() {
            @Override
            public int compare(Map.Entry<List<CourseTask>, List<Integer>> o1, Map.Entry<List<CourseTask>, List<Integer>> o2) {
                /**
                 * 这样做的目的是防止，头重脚轻的局面
                 * 假设有这样一种情况，当必要条件已经满足,即conflicts == 0
                 * 但是其非必要条件的冲突特别大,假设为_conflicts = 40
                 * 如果使用，必要条件相等的时候，再使用非必要条件进行排序这种方式，就会导致[0,40]会排在[1,0]的前面,这样就很不合理。
                 * 只用片面的条件进行比较，必要会导致片面的结果
                 * 所有我采用，当非必要条件相差不大（默认+=1的范围），则考虑是否用非必要条件进行比较
                 * 如果非必要条件相差很大（默认+=10的范围）,则使用非必要条件进行排序，否则使用必要条件进行排序。
                 */
                //如果必要条件相差不大，即差值在+-1内（包含1），则进行下面的判断
                if (Math.abs((o1.getValue().get(0) - o2.getValue().get(0))) <= 1) {
                    //如果非必要条件的差距很大，则用非必要条件进行排序
                    if (Math.abs((o1.getValue().get(1) - o2.getValue().get(1))) >= 10) {
                        return o1.getValue().get(1).compareTo(o2.getValue().get(1));
                    }
                    //否则用必要条件进行排序
                    return o1.getValue().get(0).compareTo(o2.getValue().get(0));
                }
                return o1.getValue().get(0).compareTo(o2.getValue().get(0));
            }
        });
        Iterator<Map.Entry<List<CourseTask>, List<Integer>>> iterator = list.iterator();
        Map.Entry<List<CourseTask>, List<Integer>> entry = null;
        /**
         * 记录当前精英的个数
         */
        int count = 0;
        System.out.println("\n");
        while (iterator.hasNext()) {
            entry = iterator.next();
            System.out.println(entry.getValue());
            res.put(entry.getKey(), entry.getValue());
            count++;
            if (count == elite) {
                break;
            }
        }
        System.out.println("\n");
        return res;
    }

    /**
     * 变异 —— 随机取出精英种群中 其中之一 进行变异
     *
     * @param population
     * @param classroomListSize
     * @return
     */
    private List<CourseTask> _mutate(List<List<CourseTask>> population, Integer classroomListSize) {
        //随机选择变异种群中的个体
        List<CourseTask> individual = new ArrayList<>(population.get(new Random().nextInt(population.size())));
        //变异
        for (CourseTask chromosome : individual) {
            //随机变异——染色体上的基因点
            int gene_index = new Random().nextInt(3);
            if (gene_index == 0) {
                chromosome.setClassroom(new Random().nextInt(classroomListSize));
            } else if (gene_index == 1) {
                chromosome.setWeekDay(new Random().nextInt(5));
            } else if (gene_index == 2) {
                chromosome.setCourseTime(new Random().nextInt(4));
            }
        }
        //返回变异的个体
        return individual;
    }

    /**
     * 交叉—— 随机取出精英种群中 其中之二 进行染色体部分交叉
     *
     * @param elitePopulation
     */
    private void _crossOver(List<List<CourseTask>> elitePopulation) {
        Random random = new Random();
        //随机取出两个个体
        int index = random.nextInt(elitePopulation.size());
        List<CourseTask> individual = elitePopulation.get(index);
        //防止取出相同的个体
        List<CourseTask> _individual = elitePopulation.get(random.nextInt(elitePopulation.size() - index));
        //随机交叉 —— 染色体基因点
        int gene_index = random.nextInt(3);
        Integer temp;
        CourseTask gene;
        CourseTask _gene;
        for (int i = 0; i < individual.size(); i++) {
            gene = individual.get(i);
            _gene = _individual.get(i);
            if (gene_index == 0) {
                temp = gene.getClassroom();
                gene.setClassroom(_gene.getClassroom());
                _gene.setClassroom(temp);
            } else if (gene_index == 1) {
                temp = gene.getWeekDay();
                gene.setWeekDay(_gene.getWeekDay());
                _gene.setWeekDay(temp);
            } else if (gene_index == 2) {
                temp = gene.getCourseTime();
                gene.setCourseTime(_gene.getCourseTime());
                _gene.setCourseTime(temp);
            }
        }
    }

    private static Integer get_conflicts(int[] weekDayArr, int[] courseTimeArr) {
        Integer _conflicts = 0;
        /**
         * 记录课程是否分布均匀（粗略考虑）
         * 通过迭代 优中选优
         */
        //一天有4节课，如果能保证每天只有1——2节课，则比较均匀
        for (int item : weekDayArr) {
            if (item >= 4) {
                _conflicts += 30;
            }
            if (item >= 3) {
                _conflicts += 10;
            }
            if (item == 0) {
                _conflicts += 5;
            }
        }
        //某个时间节点的课程（如，1，2小节）有5天（周一到周五）
        //如果能保证每个时间节点的课程只有1——3节，则比较均匀
        Set courseTimeSet = new HashSet();
        for (int item : courseTimeArr) {
            courseTimeSet.add(item);
            if (item >= 5) {
                _conflicts += 30;
            }
            if (item >= 4) {
                _conflicts += 15;

            }
            if (item == 0) {
                _conflicts += 5;
            }
        }
        return _conflicts;
    }
}
