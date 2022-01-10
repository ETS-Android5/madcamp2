from django.db import models

class SightType(models.IntegerChoices):
    RED = 0
    GREEN = 1
    BLUE = 2
    YELLOW = 3
    WHITE = 4
    BLACK = 5
    PURPLE = 6
    ORANGE = 7
    PINK = 8
    BROWN = 9

class TouchType(models.IntegerChoices):
    SOFT = 0
    HARD = 1
    COOL = 2
    WARM = 3
    HOT = 4
    COLD = 5

class TasteType(models.IntegerChoices):
    SWEET = 0
    SALTY = 1
    BITTER = 2
    SOUR = 3
    NUTTY = 4
    HOT = 5

class Area(models.Model):
    city = models.CharField(max_length=20)
    county = models.CharField(max_length=20)
    last = models.CharField(max_length=40)

    def __str__(self):
        return "{0} {1} {2}".format(self.city, self.county, self.last)

class Review(models.Model):
    area = models.ForeignKey(Area, on_delete=models.CASCADE)
    sight = models.IntegerField(choices=SightType.choices)
    touch = models.IntegerField(choices=TouchType.choices)
    taste = models.IntegerField(choices=TasteType.choices)

    def __str__(self):
        return "{0}/{1}".format(self.area, self.id)