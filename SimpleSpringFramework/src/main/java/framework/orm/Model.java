package framework.orm;


public abstract class Model<M> {

    public final ObjectManager<M> objects() {

        return null;
    }

    public void save() {

    }

    public void delete() {

    }

    @Override
    public String toString() {
        return this.getClass().getTypeName();
    }

}
