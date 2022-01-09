from django.db import models
from django.conf import settings

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

# Create your models here.
class Area(models.Model):
    reviewer = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
    address = models.CharField(max_length=100)
    sight = models.IntegerField(choices=SightType.choices)
    touch = models.IntegerField(choices=TouchType.choices)
    taste = models.IntegerField(choices=TasteType.choices)

    def __str__(self):
        return '{0}/{1}'.format(self.address, self.reviewer)