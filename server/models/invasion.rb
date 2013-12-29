class Invasion < ActiveRecord::Base
  belongs_to :user
  belongs_to :territory
end
