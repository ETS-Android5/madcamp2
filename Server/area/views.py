from rest_framework import viewsets
from .serializer import AreaSerializer
from .models import Area

class AreaViewSet(viewsets.ModelViewSet):
    queryset = Area.objects.all()
    serializer_class = AreaSerializer
