#ifndef AD7794_H
#define AD7794_H

void AD7790WirteReg(unsigned char byteword);
void AD7790ReadFromReg(int bytenumber);
int AD7790ReadData(int readtime);
int AD7790Read(int ret);
#endif
