package com.sys.androidkit.feature.samplecounter;

import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.codegen.OriginatingElement;
import dagger.hilt.internal.GeneratedEntryPoint;
import javax.annotation.processing.Generated;

@OriginatingElement(
    topLevelClass = CounterFragment.class
)
@GeneratedEntryPoint
@InstallIn(FragmentComponent.class)
@Generated("dagger.hilt.android.processor.internal.androidentrypoint.InjectorEntryPointGenerator")
public interface CounterFragment_GeneratedInjector {
  void injectCounterFragment(CounterFragment counterFragment);
}
