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
        //迭代 —— 通过变异交叉获取可能的解集，接着验证解集的适应度分值，当碰撞分值为0，则解已经出现
        for (int i = 0; i < MAX_ITERATION; i++) {
            List<List<CourseTask>> newPopulation = new ArrayList<>();
            LinkedHashMap<List<CourseTask>, Integer> rateMap = _rate(population, ELITE_SIZE, tasks.size());
            //遍历适应度分值，查看是否出现解
            for (List<CourseTask> key : rateMap.keySet()) {
                //出现解
                if (rateMap.get(key) == 0) {
                    return key;
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
        //没有出现解集
        return new ArrayList<>();
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
    private LinkedHashMap<List<CourseTask>, Integer> _rate(List<List<CourseTask>> population, Integer elite, Integer courseSize) {
        LinkedHashMap<List<CourseTask>, Integer> temp_res = new LinkedHashMap();
        LinkedHashMap<List<CourseTask>, Integer> res = new LinkedHashMap();
        CourseTask gene;
        CourseTask _gene;
        for (List<CourseTask> individual : population) {
            /**
             * 碰撞冲突值
             */
            int conflicts = 0;
            if (individual.get(courseSize - 1).studentTotalOverflows()) {
                conflicts++;
            }
            for (int i = 0; i < courseSize - 1; i++) {
                if (individual.get(i).studentTotalOverflows()) {
                    conflicts++;
                }
                gene = individual.get(i);
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
            temp_res.put(individual, conflicts);
        }
        List<Map.Entry<List<CourseTask>, Integer>> list = new ArrayList<>(temp_res.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<List<CourseTask>, Integer>>() {
            @Override
            public int compare(Map.Entry<List<CourseTask>, Integer> o1, Map.Entry<List<CourseTask>, Integer> o2) {
                //排序
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        Iterator<Map.Entry<List<CourseTask>, Integer>> iterator = list.iterator();
        Map.Entry<List<CourseTask>, Integer> entry = null;
        /**
         * 当前精英的个数
         */
        int count = 0;
        while (iterator.hasNext()) {
            entry = iterator.next();
            res.put(entry.getKey(), entry.getValue());
            count++;
            if (count == elite) {
                break;
            }
        }
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
        Random random = new Random();
        //随机选择变异种群中的个体
        List<CourseTask> individual = new ArrayList<>(population.get(random.nextInt(population.size())));
        //变异
        for (CourseTask chromosome : individual) {
            //随机变异——染色体上的基因点
            int gene_index = random.nextInt(3);
            if (gene_index == 0) {
                chromosome.setClassroom(random.nextInt(classroomListSize));
            } else if (gene_index == 1) {
                chromosome.setWeekDay(random.nextInt(5));
            } else if (gene_index == 2) {
                chromosome.setCourse_time(random.nextInt(4));
            }
        }
        //返回变异的个体
        return individual;
    }

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
                temp = gene.getCourse_time();
                gene.setCourse_time(_gene.getCourse_time());
                _gene.setCourse_time(temp);
            }
        }
    }
}
