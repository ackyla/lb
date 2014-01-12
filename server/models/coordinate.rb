class Coordinate < ActiveRecord::Base
  has_many :locations
  has_many :territories

  def self.find_or_create(opts)
    self.where(opts).first_or_create
  end

  def distance(loc)
    y1 = self.lat * Math::PI / 180
    x1 = self.long * Math::PI / 180
    y2 = loc.lat * Math::PI / 180
    x2 = loc.long * Math::PI / 180
    earth_r = 6378140
    deg = Math::sin(y1) * Math::sin(y2) + Math::cos(y1) * Math::cos(y2) * Math::cos(x2 - x1)
    distance = earth_r * (Math::atan(-deg / Math::sqrt(-deg * deg + 1)) + Math::PI / 2)

    return distance
  end
end
