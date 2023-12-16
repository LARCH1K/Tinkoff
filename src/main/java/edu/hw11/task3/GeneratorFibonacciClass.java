package edu.hw11.task3;

import net.bytebuddy.jar.asm.ClassWriter;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.jar.asm.Opcodes;

public class GeneratorFibonacciClass {

    private final static String CLASS_NAME = "Fibonacci";
    private final static String METHOD_NAME = "fib";
    private final static String METHOD_ARGS = "(I)J";
    private final static int MAX_STACK = 3;

    private GeneratorFibonacciClass() {
    }

    public static byte[] generateFibonacciClass() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, CLASS_NAME,
            null, "java/lang/Object", null);

        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
            METHOD_NAME, METHOD_ARGS, null, null);
        mv.visitCode();

        mv.visitVarInsn(Opcodes.ILOAD, 0);
        Label ifLeqLabel = new Label();
        mv.visitJumpInsn(Opcodes.IFLE, ifLeqLabel);

        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitInsn(Opcodes.ICONST_1);
        Label ifEqLabel = new Label();
        mv.visitJumpInsn(Opcodes.IF_ICMPEQ, ifEqLabel);

        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitInsn(Opcodes.ICONST_2);
        Label ifEqLabel2 = new Label();
        mv.visitJumpInsn(Opcodes.IF_ICMPEQ, ifEqLabel2);

        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitInsn(Opcodes.ICONST_1);
        mv.visitInsn(Opcodes.ISUB);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, CLASS_NAME, METHOD_NAME, METHOD_ARGS, false);
        mv.visitVarInsn(Opcodes.ILOAD, 0);
        mv.visitInsn(Opcodes.ICONST_2);
        mv.visitInsn(Opcodes.ISUB);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, CLASS_NAME, METHOD_NAME, METHOD_ARGS, false);

        mv.visitInsn(Opcodes.LADD);
        mv.visitInsn(Opcodes.LRETURN);

        mv.visitLabel(ifEqLabel);
        mv.visitInsn(Opcodes.LCONST_1);
        mv.visitInsn(Opcodes.LRETURN);

        mv.visitLabel(ifEqLabel2);
        mv.visitInsn(Opcodes.LCONST_1);
        mv.visitInsn(Opcodes.LRETURN);

        mv.visitLabel(ifLeqLabel);
        mv.visitInsn(Opcodes.LCONST_0);
        mv.visitInsn(Opcodes.LRETURN);

        mv.visitMaxs(MAX_STACK, 1);
        mv.visitEnd();

        cw.visitEnd();

        return cw.toByteArray();
    }
}
