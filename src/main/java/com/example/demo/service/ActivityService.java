package com.example.demo.service;

import com.example.demo.bean.Activity;
import com.example.demo.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ActivityService {
    @Resource
    private ActivityRepository activityRepository;

    public List<Activity> findAllActivities(int id){
        return activityRepository.findAllActivities(id);
    }

    public int addActivity(Activity activity){
        return activityRepository.addActivity(activity);
    }

    public int deleteActivity(int id){
        return activityRepository.deleteActivity(id);
    }

    public List<Activity> findNowActivity(int id){
        return activityRepository.findNowActivity(id);
    }

    public int addOrderActivity(List<Activity> activityList){
        return activityRepository.addOrderActivity(activityList);
    }

    public List<Activity> findActivityByUid(int id){
        return activityRepository.findAllActivitiesByUid(id);
    }

    public int removeActivity(int id){
        return activityRepository.removeActivity(id);
    }

}
