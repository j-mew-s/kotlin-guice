package com.authzee.kotlinguice.benchmarks;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.CompilerControl;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Benchmarks showing the performance of Guice bindings from Java for comparison with the kotlin
 * benchmarks.
 *
 * @author John Leacox
 */
@Fork(1)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
public class JavaInjectorBenchmark {
  @Benchmark
  public void getSimpleInstance() {
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(Simple.class).to(SimpleImpl.class);
      }
    });

    Simple instance = injector.getInstance(Simple.class);
    instance.value();
  }

  @Benchmark
  public void getComplexIterableInstance() {
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(new TypeLiteral<Complex<Iterable<String>>>() {
        })
            .to(new TypeLiteral<ComplexImpl<Iterable<String>>>() {
            });
      }
    });

    Complex<Iterable<String>> instance = injector
        .getInstance(Key.get(new TypeLiteral<Complex<Iterable<String>>>() {
        }));
    instance.value();
  }

  @Benchmark
  public void getComplexStringInstance() {
    Injector injector = Guice.createInjector(new AbstractModule() {
      @Override
      protected void configure() {
        bind(new TypeLiteral<Complex<String>>() {
        })
            .to(StringComplexImpl.class);
      }
    });

    Complex<String> instance = injector
        .getInstance(Key.get(new TypeLiteral<Complex<String>>() {
        }));
    instance.value();
  }
}
