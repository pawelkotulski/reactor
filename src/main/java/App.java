import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

public class App {

    public static void main(String... arg) {
        simpleSubscribe();
        baseSubscriberImplementation();
        mapWithFLux();
    }

    private static void mapWithFLux() {
        Flux<Integer> integerFlux = Flux.range(1, 20)
                .map(n -> {
                    if (n < 10) {
                        return n * 2;
                    }
                    throw new IllegalArgumentException("Number should not be greater than 10");
                })
                .onErrorReturn(IllegalArgumentException.class, 666);

        integerFlux.subscribe(System.out::println);

    }

    private static void baseSubscriberImplementation() {
        Flux<Integer> integerFlux = Flux.range(1, 100);
        integerFlux
                .doOnNext(r -> System.out.println("do on next " + r))
                .doOnRequest(r -> System.out.println("request of " + r))
                .subscribe(new BaseSubscriber<Integer>() {

                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    protected void hookOnNext(Integer value) {
                        System.out.println("Cancel after receive " + value);
                        cancel();
                    }
                });
    }

    private static void simpleSubscribe() {
        Flux.range(1, 100)
                .subscribe(
                        System.out::println,
                        System.out::println,
                        () -> System.out.println("DONE"),
                        subscription -> subscription.request(7)
                );
    }
}
