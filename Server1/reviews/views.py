from django.db.models.base import Model
from rest_framework.viewsets import ModelViewSet
from .models import Review, Area
from .serializer import ReviewSerializer, AreaSerializer

class ReviewViewSet(ModelViewSet):
    queryset = Review.objects.all()
    serializer_class = ReviewSerializer

class AreaViewSet(ModelViewSet):
    queryset = Area.objects.all()
    serializer_class = AreaSerializer

