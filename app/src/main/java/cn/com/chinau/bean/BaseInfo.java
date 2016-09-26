package cn.com.chinau.bean;

/**
 * Created by Administrator on 2016/8/26.
 */
public class BaseInfo {
    public String Id;
    public String name;
    public  boolean isChoosed;
    public BaseInfo()
    {
        super();
    }
    public BaseInfo(String id, String name)
    {
        super();
        Id = id;
        this.name = name;

    }

    public String getId()
    {
        return Id;
    }

    public void setId(String id)
    {
        Id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isChoosed()
    {
        return isChoosed;
    }

    public void setChoosed(boolean isChoosed)
    {
        this.isChoosed = isChoosed;
    }


}
