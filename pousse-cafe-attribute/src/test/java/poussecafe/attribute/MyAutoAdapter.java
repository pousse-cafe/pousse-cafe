package poussecafe.attribute;


public class MyAutoAdapter implements AutoAdapter<AdaptedData> {

    public static MyAutoAdapter adapt(AdaptedData input) {
        var data = new MyAutoAdapter();
        data.data = input.data;
        return data;
    }

    private String data;

    @Override
    public AdaptedData adapt() {
        var output = new AdaptedData();
        output.data = data;
        return output;
    }
}
