#pragma version(1)
#pragma rs java_package_name(pl.m4.photomaker)

rs_allocation secondImage;

void root(const uchar4 *v_in, uchar4 *v_out, uint32_t x, uint32_t y) {
    float3 leftImage = rsUnpackColor8888(*v_in).rgb;
    float3 rightImage = {0,0,0};
    float3 outputImage;

    const uchar4 *v_in2 = rsGetElementAt(secondImage, x, y);
    rightImage = rsUnpackColor8888(*v_in2).rgb;

    outputImage.r = (0.7 * leftImage.g) + (0.3 * leftImage.b);
    outputImage.g = rightImage.g;
    outputImage.b = rightImage.b;

    *v_out = rsPackColorTo8888(outputImage);
}