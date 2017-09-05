package com.emotibot.middleware.context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.emotibot.middleware.response.Response;
import com.emotibot.middleware.response.ResponseType;
import com.emotibot.middleware.task.Task;

/**
 * 
 * 一个context不一定同一时刻只在一个step中跑，可以是并行的
 * 
 * 但是一个线程必定同一个时刻只跑一个context
 * 
 * @author emotibot
 *
 */

public class Context
{
    private Map<String, Object> contextMap = new ConcurrentHashMap<String, Object>();
    private Map<ResponseType, List<Response>> outputMap = new ConcurrentHashMap<ResponseType, List<Response>>();
    private ThreadLocal<List<Task>> taskList = new ThreadLocal<List<Task>>();
    private String uniqId;
    
    public Context()
    {
        uniqId = Thread.currentThread().getName();
        taskList.set(new ArrayList<Task>());
    }
    
    public Context(String uniqId)
    {
        this.uniqId = uniqId;
        taskList.set(new ArrayList<Task>());
    }
    
    public Object getValue(String key)
    {
        return contextMap.get(key);
    }
    
    public void setValue(String key, Object value)
    {
        if (key == null || value == null)
        {
            return;
        }
        contextMap.put(key, value);
    }
    
    public List<Task> getTaskList()
    {
        return taskList.get();
    }
    
    public Map<ResponseType, List<Response>> getOutputMap()
    {
        return this.outputMap;
    }
    
    public void clearTaskList()
    {
        taskList.get().clear();
    }
    
    public void clearOutputMap()
    {
        outputMap.clear();
    }
    
    public void addTask(Task task)
    {
        taskList.get().add(task);
    }
    
    public void addOutput(ResponseType type, Response response)
    {
        List<Response> outputList = outputMap.get(type);
        if (outputList == null)
        {
            outputList = new ArrayList<Response>();
            outputMap.put(type, outputList);
        }
        outputList.add(response);
    }
    
    public boolean isContainsKey(String key)
    {
        Object obj = contextMap.get(key);
        return obj != null;
    }
    
    public String getUniqId()
    {
        return this.uniqId;
    }
}
