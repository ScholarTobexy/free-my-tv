package io.freemytv;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;

/**
 * Pictures drawn here instead of downloaded: the "Can't decide?" card's, a clapperboard in Film to Watch's pink with its
 * white heart, on the dark red to purple of its banner. At 1920×1080 like a backdrop; the card shows the middle.
 */
final class CardArt {
    private CardArt() {}

    private static final int PINK = 0xFFFF4D6D, STRIPE_DARK = 0xFF2A1830, WHITE = 0xFFFFFFFF;

    static Bitmap filmToWatch(int w, int h) {
        Bitmap out = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(out);
        float k = h / 1080f, cx = w / 2f;
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setShader(new LinearGradient(0, 0, w, h, 0xFF5B1A2B, 0xFF24143D, Shader.TileMode.CLAMP));
        c.drawRect(0, 0, w, h, p);
        p.setShader(null);

        // The board: 440 wide, under a striped bar, with the open clapper above it.
        float left = cx - 220 * k, right = cx + 220 * k, barTop = 470 * k, barBottom = 540 * k, bottom = 820 * k;
        RectF body = new RectF(left, barBottom - 20 * k, right, bottom);
        p.setColor(PINK);
        c.drawRoundRect(body, 36 * k, 36 * k, p);
        stripes(c, p, new RectF(left, barTop, right, barBottom), k);
        // The clapper, open: the same striped bar turned up from its left end.
        c.save();
        c.rotate(-16, left, barTop - 8 * k);
        stripes(c, p, new RectF(left, barTop - 78 * k, right, barTop - 8 * k), k);
        c.restore();

        // Film to Watch's heart on the board.
        float hx = cx, hy = 676 * k, s = 92 * k;
        Path heart = new Path();
        heart.moveTo(hx, hy + s * 0.95f);
        heart.cubicTo(hx - s * 1.45f, hy + s * 0.05f, hx - s * 0.95f, hy - s * 1.05f, hx, hy - s * 0.42f);
        heart.cubicTo(hx + s * 0.95f, hy - s * 1.05f, hx + s * 1.45f, hy + s * 0.05f, hx, hy + s * 0.95f);
        heart.close();
        p.setColor(WHITE);
        c.drawPath(heart, p);
        return out;
    }

    /** A clapper bar: rounded, white with dark diagonal stripes. */
    private static void stripes(Canvas c, Paint p, RectF bar, float k) {
        float r = 14 * k;
        c.save();
        Path clip = new Path();
        clip.addRoundRect(bar, r, r, Path.Direction.CW);
        c.clipPath(clip);
        p.setColor(WHITE);
        c.drawRect(bar, p);
        p.setColor(STRIPE_DARK);
        float step = 88 * k, slant = bar.height() * 0.8f;
        for (float x = bar.left - slant; x < bar.right + step; x += step) {
            Path stripe = new Path();
            stripe.moveTo(x, bar.bottom);
            stripe.lineTo(x + step / 2, bar.bottom);
            stripe.lineTo(x + step / 2 + slant, bar.top);
            stripe.lineTo(x + slant, bar.top);
            stripe.close();
            c.drawPath(stripe, p);
        }
        c.restore();
    }
}
