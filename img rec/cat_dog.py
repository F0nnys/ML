# -*- coding: utf-8 -*-
import os
from keras.models import *
from keras.layers import *
from keras.applications import *
from keras.preprocessing.image import *
import h5py
import zipfile
import argparse

def write_gap(MODEL, image_size, lambda_func=None):
    width = image_size[0]
    height = image_size[1]
    input_tensor = Input((height, width, 3))
    x = input_tensor
    if lambda_func:
        x = Lambda(lambda_func)(x)

    base_model = MODEL(input_tensor=x, weights='imagenet', include_top=False)
    model = Model(base_model.input, GlobalAveragePooling2D()(base_model.output))

    gen = ImageDataGenerator()

    train_generator = gen.flow_from_directory(train_file_path, image_size, shuffle=False,
                                              batch_size=16)
    test_generator = gen.flow_from_directory(test_file_path, image_size, shuffle=False,
                                             batch_size=16, class_mode=None)

    train = model.predict_generator(train_generator, train_generator.nb_sample)
    test = model.predict_generator(test_generator, test_generator.nb_sample)

    h5_path = os.path.join(FLAGS.model_dir, "gap_%s.h5" % MODEL.func_name)
    with h5py.File(h5_path) as h:
        h.create_dataset("train", data=train)
        h.create_dataset("test", data=test)
        h.create_dataset("label", data=train_generator.classes)


if __name__ == '__main__':
    parser = argparse.ArgumentParser()

    parser.add_argument('--data_dir', type=str, default='',
                        help='input data path')
    parser.add_argument('--model_dir', type=str, default='',
                        help='output model path')
    FLAGS, _ = parser.parse_known_args()

    train_file_path = os.path.join(FLAGS.data_dir, "train2")
    test_file_path = os.path.join(FLAGS.data_dir, "test2")

    azip = zipfile.ZipFile(train_file_path+"/dog.zip")
    czip = zipfile.ZipFile(train_file_path + "/cat.zip")
    bzip = zipfile.ZipFile(test_file_path + "/test.zip")

    azip.extractall()
    bzip.extractall()
    czip.extractall()

    #os.remove(train_file_path+"/dog.zip")
    #os.remove(train_file_path + "/cat.zip")
    #os.remove(test_file_path + "/test.zip")

    write_gap(ResNet50, (224, 224))
    write_gap(InceptionV3, (299, 299), inception_v3.preprocess_input)
    write_gap(Xception, (299, 299), xception.preprocess_input)