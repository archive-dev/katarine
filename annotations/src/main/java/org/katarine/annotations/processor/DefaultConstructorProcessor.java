package org.katarine.annotations.processor;

import com.google.auto.service.AutoService;
import org.katarine.annotations.NotInstantiatable;
import org.katarine.annotations.RequiresDefaultConstructor;
import org.katarine.annotations.Static;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("org.katarine.annotations.RequiresDefaultConstructor")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class DefaultConstructorProcessor extends AbstractProcessor {
    private Types typeUtils;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> annotatedElements = roundEnv.getRootElements();
        for (Element element : annotatedElements) {
            if (element.getKind() == ElementKind.CLASS || element.getKind() == ElementKind.INTERFACE) {
//                System.out.println(element.getSimpleName());
                TypeElement typeElement = (TypeElement) element;
                checkDefaultConstructor(typeElement);
                checkSubclasses(typeElement, roundEnv);
                checkSuperclassAndInterfaces(typeElement);
            }
        }
        return true;
    }

    private void checkDefaultConstructor(TypeElement typeElement) {
        if (typeElement.getKind()!=ElementKind.CLASS || typeElement.getAnnotation(Static.class)!=null ||
            typeElement.getAnnotation(NotInstantiatable.class)!=null ||
            typeElement.getAnnotation(RequiresDefaultConstructor.class)==null) return;
        boolean hasDefaultConstructor = false;
        
        for (ExecutableElement constructor : ElementFilter.constructorsIn(typeElement.getEnclosedElements())) {
            if (constructor.getParameters().isEmpty() && constructor.getModifiers().contains(Modifier.PUBLIC)) {
                hasDefaultConstructor = true;
                break;
            }
        }

        if (!hasDefaultConstructor) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Class " + typeElement.getSimpleName() + " must have a public default constructor.",
                    typeElement);
        }
    }

    private void checkSubclasses(TypeElement typeElement, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getRootElements()) {
            if (element.getKind() == ElementKind.CLASS) {
                TypeElement potentialSubclass = (TypeElement) element;
                if (typeUtils.isSubtype(potentialSubclass.asType(), typeElement.asType()) &&
                        !potentialSubclass.equals(typeElement) &&
                        potentialSubclass.getAnnotation(NotInstantiatable.class)==null) {
                    checkDefaultConstructor(potentialSubclass);
                }
            }
        }
    }

    private void checkSuperclassAndInterfaces(TypeElement element) {
        if (element.getKind() != ElementKind.CLASS) return;
        boolean hasAnnotation = element.getSuperclass().getAnnotation(RequiresDefaultConstructor.class)!=null
                || !element.getInterfaces().stream()
                .filter(el -> el.getAnnotation(RequiresDefaultConstructor.class) != null)
                .toList().isEmpty();

        hasAnnotation |= checkAnnotationInSuperClasses(element);
        if (!hasAnnotation) return;

        boolean hasDefaultConstructor = false;

        for (var constructor : ElementFilter.constructorsIn(element.getEnclosedElements())) {
            if (constructor.isDefault() || constructor.getParameters().isEmpty()) {
                hasDefaultConstructor = true;
                break;
            }
        }

        if (!hasDefaultConstructor) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "Class " + element.getSimpleName() + " must have a public default constructor.",
                    element);
        }
    }

    private boolean checkAnnotationInSuperClasses(TypeElement element) {
        System.out.println(element.toString());
        if (element.getSuperclass().toString().startsWith("java.lang.Object")) return false;
        if (element.getAnnotation(RequiresDefaultConstructor.class)!=null) return true;
        else {
            try {
                return checkAnnotationInSuperClasses((TypeElement) element.getSuperclass());
            } catch(ClassCastException e) {
                return element.getSuperclass().getAnnotation(RequiresDefaultConstructor.class)!=null;
            }
        }
    }
}
