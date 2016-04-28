#pragma version(1)
#pragma rs java_package_name(pl.m4.photomaker)

rs_allocation secondImage;

void root(const uchar4 *v_in, uchar4 *v_out, uint32_t x, uint32_t y) {
    float3 leftImage = rsUnpackColor8888(*v_in).rgb;
    float3 rightImage = {0,0,0};
    float3 outputImage;

    const uchar4 *v_in2 = rsGetElementAt(secondImage, x, y);
    rightImage = rsUnpackColor8888(*v_in2).rgb;

    outputImage.r = (0.4561 * leftImage.r) + (0.500484 * leftImage.g) + (0.176381 * leftImage.b) - (0.0434706 * rightImage.r) - (0.0879388 * rightImage.g) - (0.00155529 * rightImage.b);
    outputImage.g = (-0.0400822 * leftImage.r) - (0.0378246 * leftImage.g) - (0.0157589 * leftImage.b) + (0.378476 * rightImage.r) + (0.73364 * rightImage.g) - (0.0184503 * rightImage.b);
    outputImage.b = (-0.0152161 * leftImage.r) - (0.0205971 * leftImage.g) - (0.00546856 * leftImage.b) - (0.0721527 * rightImage.r) - (0.112961 * rightImage.g) + (1.2264 * rightImage.b);

    *v_out = rsPackColorTo8888(outputImage);
}
/*
Ar = 0.4561 * Lr + 0.500484 * Lg + 0.176381 * Lb - 0.0434706 * Rr - 0.0879388 * Rg - 0.00155529 * Rb
Ag = -0.0400822 * Lr - 0.0378246 * Lg - 0.0157589 * Lb + 0.378476 * Rr + 0.73364 * Rg - 0.0184503 * Rb
Ab = -0.0152161 * Lr - 0.0205971 * Lg - 0.00546856 * Lb - 0.0721527 * Rr - 0.112961 * Rg + 1.2264 * Rb
*/