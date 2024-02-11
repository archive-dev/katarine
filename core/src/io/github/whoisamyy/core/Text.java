package io.github.whoisamyy.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import io.github.whoisamyy.components.DrawableComponent;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.utils.EditorObject;
import io.github.whoisamyy.utils.Utils;

import static com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.DEFAULT_CHARS;

@EditorObject
public class Text extends DrawableComponent {
    public String text = "example text";
    public boolean show = true;

    //copied from libgdx tutorial

    /** The size in units (for some reason) */
    protected int size = 16;
    /** Foreground color (required for non-black borders) */
    protected Color color = Color.WHITE;
    /** Border width in pixels, 0 to disable */
    protected float borderWidth = 0;
    /** Border color; only used if borderWidth > 0 */
    protected Color borderColor = Color.BLACK;
    /** true for straight (mitered), false for rounded borders */
    protected boolean borderStraight = false;
    /** Offset of text shadow on X axis in pixels, 0 to disable */
    protected int shadowOffsetX = 0;
    /** Offset of text shadow on Y axis in pixels, 0 to disable */
    protected int shadowOffsetY = 0;
    /** Shadow color; only used if shadowOffset > 0 */
    protected Color shadowColor = new Color(0, 0, 0, 0.75f);
    /** The characters the font should contain */
    protected String characters = DEFAULT_CHARS;
    /** Whether the font should include kerning */
    protected boolean kerning = true;
    /** The optional PixmapPacker to use */
    protected PixmapPacker packer = null;
    /** Whether to flip the font vertically */
    protected boolean flip = false;
    /** Whether to generate mip maps for the resulting texture */
    protected boolean genMipMaps = true;
    /** Minification filter */
    protected Texture.TextureFilter minFilter = Texture.TextureFilter.Linear;
    /** Magnification filter */
    protected Texture.TextureFilter magFilter = Texture.TextureFilter.Linear;
    private BitmapFont font;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private String fontFile;
    private float sizeXY;

    private Vector2 pos = new Vector2();

    public Text(String fontFile, float sizeXY, int size, Color color, float borderWidth, Color borderColor, boolean borderStraight, int shadowOffsetX, int shadowOffsetY, Color shadowColor, String characters, boolean kerning, PixmapPacker packer, boolean flip, boolean genMipMaps, Texture.TextureFilter minFilter, Texture.TextureFilter magFilter) {
        this.fontFile = fontFile;
        this.size = size;
        this.color = color;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        this.borderStraight = borderStraight;
        this.shadowOffsetX = shadowOffsetX;
        this.shadowOffsetY = shadowOffsetY;
        this.shadowColor = shadowColor;
        this.characters = characters;
        this.kerning = kerning;
        this.packer = packer;
        this.flip = flip;
        this.genMipMaps = genMipMaps;
        this.minFilter = minFilter;
        this.magFilter = magFilter;

        this.parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        this.parameter.size = size;
        this.parameter.color = color;
        this.parameter.borderWidth = borderWidth;
        this.parameter.borderColor = borderColor;
        this.parameter.borderStraight = borderStraight;
        this.parameter.shadowOffsetX = shadowOffsetX;
        this.parameter.shadowOffsetY = shadowOffsetY;
        this.parameter.shadowColor = shadowColor;
        this.parameter.characters = characters;
        this.parameter.kerning = kerning;
        this.parameter.packer = packer;
        this.parameter.flip = flip;
        this.parameter.genMipMaps = genMipMaps;
        this.parameter.minFilter = minFilter;
        this.parameter.magFilter = magFilter;
        this.sizeXY = sizeXY;
    }

    /**
     *
     * @param fontFile
     * @param sizeXY
     * @param size breaks on size > ~128
     * @param color
     * @param borderWidth
     * @param borderColor
     * @param borderStraight
     */
    public Text(String fontFile, float sizeXY, int size, Color color, float borderWidth, Color borderColor, boolean borderStraight) {
        this(fontFile, sizeXY, size, color, borderWidth, borderColor, borderStraight, 0, 0, Color.BLACK, DEFAULT_CHARS, true, null, false, false, Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    public Text(String fontFile, float size, Color color, float borderWidth, Color borderColor, boolean borderStraight) {
        this(fontFile, size/ Utils.PPU, 128, color, borderWidth, borderColor, borderStraight, 0, 0, Color.BLACK, DEFAULT_CHARS, true, null, false, false, Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    @Override
    public void awake() {
        font = new FreeTypeFontGenerator(Gdx.files.internal(fontFile)).generateFont(this.parameter);
        font.getData().setScale(sizeXY);
        font.setUseIntegerPositions(false);
    }

    @Override
    public void start() {
        pos.add(transform.pos);
    }

    @Override
    protected void draw() {
        Vector2 v2 = transform.pos.cpy().add(pos);
        font.draw(Editor.getInstance().getBatch(), text, v2.x, v2.y);
    }

    @Override
    public void die() {
        if (font!=null)
            font.dispose();
    }
}