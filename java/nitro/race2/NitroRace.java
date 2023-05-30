package nitro.race2;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import java.util.Timer;
import java.util.TimerTask;

class NitroRace extends View {
	boolean apertandoDir, apertandoEsq, menuScreen;
	int delayColisao, qtdFases = 6, acabamentocurva = 20, angulo, anguloOponente, fase;
	float posOponenteX, posOponenteY, screenWidth, screenHeight, posicaoX, posicaoY, fractionScreenSize;
	String debug = "";
	Bitmap btndir, btnesq, carro;
	int[] angulosIniciais = new int[]{270, 0, 90, 180, 180, 0, 270, 0};
	int[] fatorDeslocamentoOponenteX = new int[]{0, 0, 0, -9, 9};
	int[] fatorDeslocamentoOponenteY = new int[]{0, 9, -9, 0, 0};
	float[] velocidades = new float[]{0.5f, 0.54f, 0.58f, 0.62f, 0.66f, 0.68f};
	float[] velocidadesOponente = new float[]{0.41f, 0.48f, 0.5f, 0.54f, 0.58f, 0.61f};
	String[] pontos = new String[]{".", ":", ": .", ": :", ": : .", ": : :"};
	Bitmap[] imgOponente = new Bitmap[4], imagePista = new Bitmap[4];
	int[][] indicaInicio = new int[][]{{2, 0}, {4, 2}, {0, 10}, {0, 16}, {4, 12}, {14, 14}, {8, 0}, {6, 2}};
	int[][][] tracados = new int[][][]{
		{{0, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 2}, {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, {4, 4, 2, 0, 1, 0, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 2}, {0, 0, 2, 0, 1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2}, {0, 0, 2, 0, 1, 0, 2, 0, 4, 4, 4, 4, 4, 4, 2, 0, 1, 0, 2}, {1, 0, 2, 0, 1, 3, 3, 0, 1, 0, 0, 0, 0, 0, 2, 0, 1, 0, 2}, {1, 0, 2, 0, 0, 0, 0, 0, 1, 0, 2, 3, 3, 0, 2, 0, 1, 0, 2}, {1, 0, 4, 4, 4, 4, 4, 4, 1, 0, 2, 0, 1, 0, 4, 4, 1, 0, 2}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 2}, {1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 1, 3, 3, 3, 3, 3, 3}},
		{{2, 3, 3, 3, 3, 3, 3, 0, 2, 3, 3, 3, 3, 0, 2, 3, 3, 3, 3, 3}, {2, 0, 0, 0, 0, 0, 1, 0, 2, 0, 0, 0, 1, 0, 2, 0, 0, 0, 0, 1}, {2, 0, 4, 4, 2, 0, 1, 0, 4, 4, 2, 0, 1, 3, 3, 0, 0, 0, 0, 1}, {2, 0, 1, 0, 2, 0, 1, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {0, 0, 1, 0, 2, 0, 1, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 2, 0, 1}, {0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1}, {2, 3, 3, 3, 3, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1}, {2, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1}, {4, 4, 4, 4, 2, 0, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 1}, {0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {0, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1}},
		{{0, 0, 2, 3, 3, 3, 3, 3, 3, 3, 3}, {0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 0, 2, 0, 4, 4, 4, 4, 4, 4, 2}, {0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 2}, {0, 0, 2, 0, 1, 0, 2, 3, 3, 0, 2}, {0, 0, 2, 0, 1, 0, 2, 0, 1, 0, 2}, {2, 3, 3, 0, 1, 0, 2, 0, 1, 0, 2}, {2, 0, 0, 0, 1, 0, 2, 0, 1, 0, 2}, {4, 4, 4, 4, 1, 0, 2, 0, 1, 0, 2}, {0, 0, 0, 0, 0, 0, 2, 0, 1, 0, 2}, {0, 3, 3, 3, 3, 3, 3, 0, 1, 0, 2}, {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2}, {4, 4, 4, 4, 4, 4, 4, 4, 1, 0, 2}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, {1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3}},
		{{2, 3, 3, 0, 2, 3, 3, 0, 2, 3, 3, 3, 3, 3, 3, 0, 2}, {2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 0, 0, 0, 0, 1, 0, 2}, {2, 0, 1, 0, 2, 0, 1, 3, 3, 0, 4, 4, 2, 0, 1, 3, 3}, {2, 0, 1, 0, 2, 0, 0, 0, 0, 0, 1, 0, 2, 0, 0, 0, 0}, {2, 0, 1, 0, 4, 4, 4, 4, 4, 4, 1, 0, 4, 4, 4, 4, 2}, {2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, {0, 0, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3}},
		{{2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3}, {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1}, {4, 4, 4, 4, 2, 0, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1}, {0, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {4, 4, 0, 0, 2, 0, 1, 0, 2, 3, 3, 0, 2, 0, 4, 4, 2}, {1, 0, 0, 0, 2, 0, 1, 0, 2, 0, 1, 0, 2, 0, 1, 0, 2}, {1, 0, 2, 3, 3, 0, 1, 0, 2, 0, 1, 3, 3, 0, 1, 0, 2}, {1, 0, 2, 0, 0, 0, 1, 0, 2, 0, 0, 0, 0, 0, 1, 0, 2}, {1, 0, 4, 4, 2, 0, 1, 0, 4, 4, 4, 4, 4, 4, 1, 0, 2}, {1, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2}, {1, 3, 3, 3, 3, 0, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3}},
		{{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 2, 0, 0, 0, 0}, {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0}, {1, 0, 2, 3, 3, 3, 3, 0, 2, 3, 3, 0, 2, 0, 0, 0, 0}, {1, 0, 2, 0, 0, 0, 1, 0, 2, 0, 1, 0, 2, 0, 0, 0, 0}, {1, 0, 2, 0, 4, 4, 1, 0, 2, 0, 1, 0, 2, 0, 0, 0, 0}, {1, 0, 2, 0, 1, 0, 0, 0, 2, 0, 1, 0, 2, 0, 0, 0, 0}, {1, 0, 2, 0, 1, 0, 2, 3, 3, 0, 1, 0, 2, 0, 0, 0, 0}, {1, 0, 2, 0, 1, 0, 2, 0, 0, 0, 1, 0, 2, 0, 0, 0, 0}, {1, 0, 2, 0, 1, 0, 4, 4, 2, 0, 1, 0, 4, 4, 4, 4, 2}, {1, 0, 2, 0, 1, 0, 0, 0, 2, 0, 1, 0, 0, 0, 0, 0, 2}, {1, 0, 2, 0, 1, 3, 3, 3, 3, 0, 1, 0, 2, 3, 3, 0, 2}, {1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 0, 1, 0, 2}, {1, 0, 4, 4, 4, 4, 4, 4, 2, 0, 1, 0, 2, 0, 1, 0, 2}, {1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 1, 0, 2, 0, 1, 0, 2}, {1, 3, 3, 3, 3, 3, 3, 3, 3, 0, 1, 3, 3, 0, 1, 0, 0}}};

	public NitroRace(Context context) {
		super(context);
	}

	TimerTask gameLoop = new TimerTask() {
		public void run() {
			if (!menuScreen) {
				if (apertandoDir) {
					angulo = (angulo - 2 + 360) % 360;
				}
				if (apertandoEsq) {
					angulo = (angulo + 2) % 360;
				}

				//curva do oponente
				if (
					-posOponenteY / 200 / fractionScreenSize > 0 &&
					-posOponenteX / 200 / fractionScreenSize > 0 &&
					-posOponenteY / 200 / fractionScreenSize < tracados[fase].length &&
					-posOponenteX / 200 / fractionScreenSize < tracados[fase][0].length &&
					tracados[fase][(int)(-posOponenteY / 200 / fractionScreenSize)][(int)(-posOponenteX / 200 / fractionScreenSize)] != 0) {
						if (acabamentocurva < 20) acabamentocurva++;
						else {
							acabamentocurva = 0;
							anguloOponente = tracados[fase][(int)(-posOponenteY / 200 / fractionScreenSize)][(int)(-posOponenteX / 200 / fractionScreenSize)];
						}
				}

				//identifica sobre qual quadradinho de pista o carro esta nesse frame
				int[] sobrePista = new int[]{-1, -1};
				if (
					-posicaoY / 200 / fractionScreenSize > 0 &&
					-posicaoX / 200 / fractionScreenSize > 0 &&
					-posicaoY / 200 / fractionScreenSize < tracados[fase].length &&
					-posicaoX / 200 / fractionScreenSize < tracados[fase][0].length &&
					tracados[fase][(int)(-posicaoY / 200 / fractionScreenSize)][(int)(-posicaoX / 200 / fractionScreenSize)] != 0) {
						sobrePista[0] = (int)(-posicaoY / 200 / fractionScreenSize);
						sobrePista[1] = (int)(-posicaoX / 200 / fractionScreenSize);
				}

				//grama
				if (sobrePista[0] == -1) {
					menuScreen = true;
				}

				//colisao
				if (Math.sqrt((posicaoX - posOponenteX) * (posicaoX - posOponenteX) + (posicaoY - posOponenteY) * (posicaoY - posOponenteY)) < 40 * fractionScreenSize) {
					delayColisao = 20;
				}

				if (delayColisao > 0 ) delayColisao--; //delay da batida
				else {
					//atualiza posicao do carro (se ele estiver no asfalto)
					posicaoX += fractionScreenSize * Math.sin(Math.toRadians(angulo)) * velocidades[fase] * 11;
					posicaoY += fractionScreenSize * Math.cos(Math.toRadians(angulo)) * velocidades[fase] * 11;
				}

				//atualiza posicao do oponente
				posOponenteX -= fractionScreenSize * fatorDeslocamentoOponenteX[anguloOponente] * velocidadesOponente[fase] * 1.4;
				posOponenteY += fractionScreenSize * fatorDeslocamentoOponenteY[anguloOponente] * velocidadesOponente[fase] * 1.4;
			}
			invalidate();//call onDraw
		}
	};

	public boolean onTouchEvent(MotionEvent motionEvent) {
		float x = motionEvent.getX();
		float y = motionEvent.getY();

		if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
			apertandoEsq = false;
			apertandoDir = false;
			if (menuScreen && y < screenHeight * .58) {
				if (x < 0.26 * screenWidth) {
					fase = (fase - 1 + qtdFases) % qtdFases;
				}
				else if (x > screenWidth - (0.26 * screenWidth)) {
					fase = (fase + 1) % qtdFases;
				}
				else { //inicializa variaveis para corrida
					delayColisao = 60;

					angulo = angulosIniciais[fase];
					switch (angulosIniciais[fase]) {
						case 0: anguloOponente = 1; break;
						case 90: anguloOponente = 3; break;
						case 180: anguloOponente = 2; break;
						case 270: anguloOponente = 4; break;
					}

					//posicao inicial do carro
					posicaoX = (200 * -indicaInicio[fase][1]) * fractionScreenSize - 100 * fractionScreenSize;
					posicaoY = (200 * -indicaInicio[fase][0]) * fractionScreenSize - 100 * fractionScreenSize;

					//posicao inicial do oponente
					posOponenteX = (200 * -indicaInicio[fase][1]) * fractionScreenSize - 100 * fractionScreenSize;
					posOponenteY = (200 * -indicaInicio[fase][0]) * fractionScreenSize - 100 * fractionScreenSize;

					//distancia entre os carros
					if (angulo == 0) {
						posicaoX -= 40 * fractionScreenSize;
						posOponenteX += 40 * fractionScreenSize;
					}
					if (angulo == 180) {
						posicaoX += 40 * fractionScreenSize;
						posOponenteX -= 40 * fractionScreenSize;
					}
					if (angulo == 90) {
						posicaoY += 40 * fractionScreenSize;
						posOponenteY -= 40 * fractionScreenSize;
					}
					if (angulo == 270) {
						posicaoY -= 40 * fractionScreenSize;
						posOponenteY += 40 * fractionScreenSize;
					}

					menuScreen = false;
				}
			}
		}
		else {
			//mudar a rota do carrinho
			if (x < screenWidth / 2) apertandoEsq = true; //user tocou na metade esquerda da tela de gameplay
			else apertandoDir = true; //user tocou na metade direita da tela de gameplay
		}
		return true;
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setColor(Color.rgb(52, 52, 52));
		canvas.drawPaint(paint);

		if (menuScreen) {
			paint.setColor(Color.rgb(255, 255, 255));
			paint.setTextAlign(Paint.Align.CENTER);
			paint.setTextSize((float)(0.1 * screenHeight));
			canvas.drawText(pontos[fase], screenWidth / 2, (float)(screenHeight / 2.32), paint);

			canvas.drawRect(34 * fractionScreenSize, (float)((screenHeight / 2.6) - 16 * fractionScreenSize), 116 * fractionScreenSize, (float)(screenHeight / 2.6) + 66 * fractionScreenSize, paint);
			canvas.drawRect(screenWidth - 116 * fractionScreenSize, (float)((screenHeight / 2.6) - (16 * fractionScreenSize)), screenWidth - 34 * fractionScreenSize, (float)((screenHeight / 2.6) + 66 * fractionScreenSize), paint);
			canvas.drawBitmap(btnesq, 50 * fractionScreenSize, (float)(screenHeight / 2.6), paint);
			canvas.drawBitmap(btndir, screenWidth - 100 * fractionScreenSize, (float)(screenHeight / 2.6), paint);
		}
		else {
			canvas.save();
			canvas.rotate(angulo, screenWidth / 2, screenHeight / 2);
			for (int y = 0; y < tracados[fase].length; y++)
				for (int x = 0; x < tracados[fase][0].length; x++)
					if (tracados[fase][y][x] != 0)
						canvas.drawBitmap(imagePista[tracados[fase][y][x] - 1], screenWidth / 2 + posicaoX + (200 * x) * fractionScreenSize, screenHeight / 2 + posicaoY + (200 * y) * fractionScreenSize, paint);
			canvas.drawBitmap(imgOponente[anguloOponente - 1], screenWidth / 2 + (-posOponenteX + posicaoX) - (carro.getWidth() / 2), screenHeight / 2 + (-posOponenteY + posicaoY) - (carro.getHeight() / 2), paint);
			canvas.restore();
			canvas.drawBitmap(carro, screenWidth / 2 - (carro.getWidth() / 2), screenHeight / 2 - (carro.getHeight() / 2), paint);
		}
		if (!debug.equals("")) {
			paint.setColor(Color.rgb(0, 35, 80));
			paint.setTextSize((float)(0.03 * screenHeight));
			canvas.drawText(debug, 100, 100, paint);
		}
	}

	protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) { //called when app start
		super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
		screenWidth = newWidth;
		screenHeight = newHeight;
		fractionScreenSize = screenWidth / 600;
		Resources resources = getResources();
		carro = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.o1), (int)(72 * fractionScreenSize), (int)(72 * fractionScreenSize), true);
		imgOponente[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.o1), (int)(72 * fractionScreenSize), (int)(72 * fractionScreenSize), true);
		imgOponente[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.o2), (int)(72 * fractionScreenSize), (int)(72 * fractionScreenSize), true);
		imgOponente[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.o3), (int)(72 * fractionScreenSize), (int)(72 * fractionScreenSize), true);
		imgOponente[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.o4), (int)(72 * fractionScreenSize), (int)(72 * fractionScreenSize), true);
		imagePista[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.p1), (int)(200 * fractionScreenSize), (int)(200 * fractionScreenSize), true);
		imagePista[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.p2), (int)(200 * fractionScreenSize), (int)(200 * fractionScreenSize), true);
		imagePista[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.p3), (int)(200 * fractionScreenSize), (int)(200 * fractionScreenSize), true);
		imagePista[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.p4), (int)(200 * fractionScreenSize), (int)(200 * fractionScreenSize), true);
		btnesq = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.btnesq), (int)(50 * fractionScreenSize), (int)(50 * fractionScreenSize), true);
		btndir = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.btndir), (int)(50 * fractionScreenSize), (int)(50 * fractionScreenSize), true);
		new Timer().schedule(gameLoop, 0, 10);
	}
}
