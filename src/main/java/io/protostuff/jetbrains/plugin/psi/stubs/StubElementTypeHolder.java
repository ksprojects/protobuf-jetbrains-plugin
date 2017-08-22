package io.protostuff.jetbrains.plugin.psi.stubs;

import com.intellij.psi.stubs.IStubElementType;

/**
 * Interface containing all [IStubElementType]s.
 */
public interface StubElementTypeHolder {
  IStubElementType ENUM = EnumStub.TYPE;
  IStubElementType GROUP = GroupStub.TYPE;
  IStubElementType MESSAGE = MessageStub.TYPE;
}
