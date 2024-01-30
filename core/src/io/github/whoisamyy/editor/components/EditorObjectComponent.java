package io.github.whoisamyy.editor.components;

import com.badlogic.gdx.physics.box2d.PolygonShape;
import io.github.whoisamyy.components.Component;
import io.github.whoisamyy.components.TriggerBox;
import io.github.whoisamyy.utils.HideInInspector;
import io.github.whoisamyy.utils.Utils;

@HideInInspector
public class EditorObjectComponent extends Component { //unintuitive name. bad


    @Override
    public void awake() {


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.7f/2, 0.7f/2);

        gameObject.addComponent(new TriggerBox(shape));
    }
}
